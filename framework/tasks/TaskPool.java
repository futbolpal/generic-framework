package framework.tasks;


public class TaskPool{
	private static TaskPool this_;

	private static int MAX_TASKS = 10;

	private Queue idlers_;

	private ThreadPoolWorker[] workers_;

	protected TaskPool(int numberOfThreads) {
		// make sure that it's at least one
		numberOfThreads = Math.max(1, numberOfThreads);

		idlers_ = new Queue(numberOfThreads);
		workers_ = new ThreadPoolWorker[numberOfThreads];

		for (int i = 0; i < workers_.length; i++) {
			workers_[i] = new ThreadPoolWorker(idlers_);
		}
	}

	public synchronized double calculatePercentDone() {
		int count = 0;
		double average = 0;
		for (ThreadPoolWorker tpw : workers_) {
			Task t = tpw.getTask();
			if (t != null) {
				if (t.isIndeterminate())
					return -1;
				average = (average * count + ((double) t.getProgress() / (double) t
						.getLength()))
						/ ++count;
			}
		}
		return average;
	}

	public synchronized double calculateAverageTime() {
		int count = 0;
		double average = 0;
		for (ThreadPoolWorker tpw : workers_) {
			Task t = tpw.getTask();
			if (t != null) {
				if (t.isIndeterminate())
					return -1;
				average = (average * count + t.getAverageTime()) / (++count);
			}
		}
		return average;
	}

	public void addTask(Task target)  {
		try {
			// block (forever) until a worker is available
			ThreadPoolWorker worker = (ThreadPoolWorker) idlers_.remove();
			worker.process(target);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized int getTaskCount()
	{
		int count = 0;
		for(ThreadPoolWorker tpw:workers_)
		{
			count+=(tpw.getTask()==null)?0:1;
		}
		return count;
	}

	public void stopRequestIdleWorkers() {
		try {
			Object[] idle = idlers_.removeAll();
			for (int i = 0; i < idle.length; i++) {
				((ThreadPoolWorker) idle[i]).stopRequest();
			}
		} catch (InterruptedException x) {
			Thread.currentThread().interrupt(); // re-assert
		}
	}

	public void stopRequestAllWorkers() {
		stopRequestIdleWorkers();

		try {
			Thread.sleep(250);
		} catch (InterruptedException x) {
		}

		for (int i = 0; i < workers_.length; i++) {
			if (workers_[i].isAlive()) {
				workers_[i].stopRequest();
			}
		}
	}

	public static TaskPool instance() {
		if (this_ == null) {
			this_ = new TaskPool(MAX_TASKS);
		}
		return this_;
	}
}

class ThreadPoolWorker extends Object {

	private Queue idlers_;

	private Queue holdbox_;

	private Thread internal_thread_;

	private boolean continue_;

	private Task task_;

	public ThreadPoolWorker(Queue idleWorkers) {
		idlers_ = idleWorkers;
		holdbox_ = new Queue(1); // only one slot

		// just before returning, the thread should be created and started.
		continue_ = true;

		Runnable r = new Runnable() {
			public void run() {
				try {
					runWork();
				} catch (Exception x) {
					// in case ANY exception slips through
					x.printStackTrace();
				}
			}
		};

		internal_thread_ = new Thread(r);
		internal_thread_.start();
	}

	public boolean isBusy() {
		return holdbox_.isEmpty();
	}

	public void process(Runnable target) throws InterruptedException {
		holdbox_.add(target);
	}

	private void runWork() {
		while (continue_) {
			try {
				idlers_.add(this);
				task_ = (Task) holdbox_.remove();
				runIt(task_);
				task_ = null;
			} catch (InterruptedException x) {
				Thread.currentThread().interrupt(); // re-assert
			}
		}
	}

	private void runIt(Runnable r) {
		try {
			r.run();
		} catch (Exception runex) {
			runex.printStackTrace();
		} finally {
			Thread.interrupted();
		}
	}

	public Task getTask() {
		return task_;
	}

	public void stopRequest() {
		continue_ = false;
		internal_thread_.interrupt();
	}

	public boolean isAlive() {
		return internal_thread_.isAlive();
	}
}

class Queue extends Object {
	private Object[] queue;

	private int capacity;

	private int size;

	private int head;

	private int tail;

	public Queue(int cap) {
		capacity = (cap > 0) ? cap : 1; // at least 1
		queue = new Object[capacity];
		head = 0;
		tail = 0;
		size = 0;
	}

	public int getCapacity() {
		return capacity;
	}

	public synchronized int getSize() {
		return size;
	}

	public synchronized boolean isEmpty() {
		return (size == 0);
	}

	public synchronized boolean isFull() {
		return (size == capacity);
	}

	public synchronized void add(Object obj) throws InterruptedException {

		waitWhileFull();

		queue[head] = obj;
		head = (head + 1) % capacity;
		size++;

		notifyAll();
	}

	public synchronized void addEach(Object[] list) throws InterruptedException {

		for (int i = 0; i < list.length; i++) {
			add(list[i]);
		}
	}

	public synchronized Object remove() throws InterruptedException {

		waitWhileEmpty();

		Object obj = queue[tail];

		queue[tail] = null;

		tail = (tail + 1) % capacity;
		size--;

		notifyAll();

		return obj;
	}

	public synchronized Object[] removeAll() throws InterruptedException {

		Object[] list = new Object[size];

		for (int i = 0; i < list.length; i++) {
			list[i] = remove();
		}

		return list;
	}

	public synchronized Object[] removeAtLeastOne() throws InterruptedException {

		waitWhileEmpty();
		return removeAll();
	}

	public synchronized boolean waitUntilEmpty(long msTimeout)
			throws InterruptedException {

		if (msTimeout == 0L) {
			waitUntilEmpty();
			return true;
		}

		long endTime = System.currentTimeMillis() + msTimeout;
		long msRemaining = msTimeout;

		while (!isEmpty() && (msRemaining > 0L)) {
			wait(msRemaining);
			msRemaining = endTime - System.currentTimeMillis();
		}

		return isEmpty();
	}

	public synchronized void waitUntilEmpty() throws InterruptedException {

		while (!isEmpty()) {
			wait();
		}
	}

	public synchronized void waitWhileEmpty() throws InterruptedException {

		while (isEmpty()) {
			wait();
		}
	}

	public synchronized void waitUntilFull() throws InterruptedException {

		while (!isFull()) {
			wait();
		}
	}

	public synchronized void waitWhileFull() throws InterruptedException {

		while (isFull()) {
			wait();
		}
	}
}