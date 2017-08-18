package info.xiaomo.gameCore.base.concurrent.executor;

import info.xiaomo.gameCore.base.concurrent.AbstractCommand;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueueMonitor {


    public static boolean open = true;

    private Map<String, QueueCount> commandCountMap = new HashMap<>();
    private Map<String, QueueCount> command50CountMap = new HashMap<>();
    private Map<String, QueueCount> command100CountMap = new HashMap<>();
    private Map<String, QueueCount> command200CountMap = new HashMap<>();
    private Map<String, QueueCount> command500CountMap = new HashMap<>();
    private Map<String, QueueCount> command1000CountMap = new HashMap<>();
    private Map<String, QueueCount> command2000CountMap = new HashMap<>();
    private Map<String, QueueCount> command10000CountMap = new HashMap<>();

    private static final Map<String, QueueMonitor> INSTANCES = new ConcurrentHashMap<>();

    public static QueueMonitor newInstance(String name) {
        QueueMonitor instance = new QueueMonitor();
        INSTANCES.put(name, instance);
        return instance;
    }

    public void monitor(AbstractCommand command, long startTime, int totalTime) {

        String key = command.getClass().getName();
        if(totalTime > 10000) {
            addCount(command10000CountMap, key, totalTime,startTime);
        } else if(totalTime > 2000) {
            addCount(command2000CountMap, key, totalTime,startTime);
        } else if (totalTime > 1000) {
            addCount(command1000CountMap, key, totalTime,startTime);
        } else if( totalTime > 500) {
            addCount(command500CountMap, key,totalTime,startTime);
        } else if(totalTime > 200) {
            addCount(command200CountMap, key, totalTime,startTime);
        } else if(totalTime > 100) {
            addCount(command100CountMap, key, totalTime,startTime);
        } else if(totalTime > 50) {
            addCount(command50CountMap, key, totalTime,startTime);
        }
        addCount(commandCountMap, key, totalTime, startTime);
    }

    private void addCount(Map<String, QueueCount> map, String key, int totalLTime, long startTime) {
        QueueCount curCount = map.get(key);
        if(curCount == null) {
            curCount = new QueueCount();
        }
        if(totalLTime > curCount.max) {
            curCount.max = totalLTime;
            curCount.maxTime = startTime;
        }
        curCount.count += 1;

        map.put(key, curCount);

    }

    public static void dump() {
        for(String key  : INSTANCES.keySet()) {
            dump(key);
        }
    }

    public static String dump(String name) {

        QueueMonitor monitor = INSTANCES.get(name);

        if(monitor == null) {
            return "没有监视器";
        }

        long time = System.currentTimeMillis();
        String fileName = name +".queue";

        StringBuilder sb = new StringBuilder();
        map2String(sb,monitor.command50CountMap,"command50CountMap" );
        map2String(sb,monitor.command100CountMap,"command100CountMap" );
        map2String(sb,monitor.command200CountMap,"command200CountMap" );
        map2String(sb,monitor.command500CountMap,"command500CountMap" );
        map2String(sb,monitor.command1000CountMap,"command1000CountMap" );
        map2String(sb,monitor.command2000CountMap,"command2000CountMap" );
        map2String(sb,monitor.command10000CountMap,"command10000CountMap" );
        PrintWriter pw = null;
        try {
            File file = new File(fileName);
            pw = new PrintWriter(file);
            pw.println(sb.toString());
        } catch (Exception e) {

        } finally{
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
        return fileName;
    }

    private static void map2String(StringBuilder sb, Map<String, QueueCount> map, String name) {
        sb.append("-----------[").append(name).append("]-----------\n");
        for(String key : map.keySet()) {
            sb.append(key).append(":").append(map.get(key).toString()).append("\n");
        }
        sb.append("\n");
    }

    static class QueueCount {
        int count;
        int max;//最大那一次的执行时间
        long maxTime;//最大那一次的开始时间

        public String toString() {
            return new StringBuilder("[")
                    .append("count:").append(count)
                    .append(",max:").append(max)
                    .append(",maxTime:").append(maxTime)
                    .append("]").toString();
        }
    }


}
