package info.xiaomo.gengine.map;

import java.util.Arrays;

/** 
 * 开启节点的二叉堆
 * @author zhangli
 * 2017年6月6日 下午9:53:42   
 */
public class OpenPointBinaryHeap {

	private int capacity;

	private int size = 0;

	private MapPoint[] array;

	public OpenPointBinaryHeap(int capacity) {
		super();
		this.capacity = capacity;
		this.array = new MapPoint[this.capacity];
	}

	public void push(MapPoint e) {
		if (size == capacity) {
			throw new RuntimeException("The heap has not enough space!");
		}
		array[size] = e;
		this.size++;
		int fatherIndex = (size >> 1) - 1; // 由于数组索引下表是从0开始的， 所以，这里 左孩子：2i+1,
											// 右孩子：2i+2
		int currentIndex = size - 1;
		siftUp(fatherIndex, currentIndex);
	}

	private void siftUp(int fatherIndex, int currentIndex) {
		while (fatherIndex >= 0) {
			if (array[fatherIndex].getF() >= array[currentIndex].getF()) {
				MapPoint temp = array[fatherIndex];
				array[fatherIndex] = array[currentIndex];
				array[currentIndex] = temp;

				currentIndex = fatherIndex;
				fatherIndex = ((currentIndex + 1) >> 1) - 1;
			} else {
				break;
			}
		}
	}

	public MapPoint pop() {
		if (size <= 0) {
			throw new RuntimeException("The heap is empty!");
		}
		MapPoint ret = array[0];
		array[0] = array[size - 1];
		array[size - 1] = null;
		size--;
		
		if (size == 0) {
			return ret;
		}

		int currentIndex = 0;
		int childIndex = (currentIndex << 1) + 1; // 由于数组索引下表是从0开始的， 所以，这里
													// 左孩子：2i+1, 右孩子：2i+2
		while (childIndex < size) {

			if (childIndex + 1 < size && array[childIndex].getF() > array[childIndex + 1].getF()) {
				childIndex++;
			}

			if (array[currentIndex].getF() <= array[childIndex].getF()) {
				break;
			}

			MapPoint temp = array[currentIndex];
			array[currentIndex] = array[childIndex];
			array[childIndex] = temp;

			currentIndex = childIndex;
			childIndex = (currentIndex << 1) + 1;
		}

		return ret;
	}

	public void update(MapPoint Point) {

		int currentIndex = -1;
		for (int i = 0; i < size; i++) {
			MapPoint pPoint = array[i];
			if (pPoint == Point) { // Point在整个地图中是唯一的，所以，这里为了速度快，直接用等号
				currentIndex = i;
				break;
			}
		}

		if (currentIndex == -1) {
			return;
		}

		// f值只会变小，不会变大，所以只要和父节点比较就可以了。
		int fatherIndex = ((currentIndex + 1) >> 1) - 1;
		siftUp(fatherIndex, currentIndex);

	}

	public void clear() {
		for (int i = 0; i < size; i++) {
			array[i] = null;
		}
		this.size = 0;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public static void main(String[] args) {
		OpenPointBinaryHeap heap = new OpenPointBinaryHeap(20);
		MapPoint Point1 = new MapPoint(1, 2);
		Point1.setF(90);
		heap.push(Point1);
		MapPoint Point2 = new MapPoint(4, 5);
		Point2.setF(30);
		heap.push(Point2);
		MapPoint Point3 = new MapPoint(1, 3);
		Point3.setF(100);
		heap.push(Point3);
		MapPoint Point4 = new MapPoint(4, 2);
		Point4.setF(60);
		heap.push(Point4);
		System.out.println(Arrays.toString(heap.array));
		Point3.setF(5);
		heap.update(Point3);
		System.out.println(Arrays.toString(heap.array));
		heap.pop();
		System.out.println(Arrays.toString(heap.array));

	}

}
