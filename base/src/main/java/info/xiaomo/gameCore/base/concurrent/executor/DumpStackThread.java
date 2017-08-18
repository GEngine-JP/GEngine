package info.xiaomo.gameCore.base.concurrent.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class DumpStackThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpStackThread.class);

    private static final AtomicInteger I = new AtomicInteger(0);

    public void run() {
        //自己的dump
        String ret = dump();
        LOGGER.error(ret);
        //jstack dump
        osDump();
    }

    public static String dump ()  {
        Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
        StringBuilder builder = new StringBuilder();
        for(Thread thread : threads.keySet()) {
            StackTraceElement[] stackTrace = thread.getStackTrace();
            builder.append("\""+thread.getName()+"\" prio="+thread.getPriority()+" tid="+thread.getId() + "\n");
            builder.append("\t" + thread.getState() + "\n");
            for(StackTraceElement se : stackTrace) {
                builder.append("\t\tat " + se + "\n");
            }
            builder.append("\n\n");
        }


        return builder.toString();
    }

    public static String osDump() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String[] arr = name.split("@");
        String pid = arr[0];
        String fileName = "os_dump_" + (System.currentTimeMillis() / 1000) + "_" + I.incrementAndGet() +".stack";
        String[] command;
        if(System.getProperty("os.name").contains("Windows")) {
            command =  new String[] { "cmd.exe", "/C", "jstack " + pid + ">" + fileName};
        } else {
            command =  new String[] { "/bin/sh", "-c", "jstack " + pid + ">" + fileName};
        }

        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
