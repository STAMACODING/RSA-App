package com.stamacoding.rsaApp.network.global.service.executor;

import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.service.Service;

public abstract class ExecutorService extends Service{
	private volatile IndexedMap<Callable<Object>, Object> callables = new IndexedMap<>();
	
	@Override
	public void onRepeat() {
		if(getCallables().size() > 0) {
			try {
				boolean waitingExecute = getCallables().getValue(0) == null;
				Callable<Object> c = getCallables().getKey(0);
				Object res = getCallables().getKey(0).call();
				getCallables().put(0, res);
				
				if(waitingExecute) {
					synchronized (c) {
						c.notify();
						c.wait();
						getCallables().remove(0);
					}
				}else {
					getCallables().remove(0);
				}
			} catch (Exception e) {
				L.f(getServiceClass(), "Callable failed!", e);
			}
		}
	}
	
	public Object executeAndWait(Callable<Object> c) {
		getCallables().put(c, null);
		
		synchronized(c) {
			try {
				c.wait();
				
				int index = getCallables().indexOf(c);
				Object res = getCallables().getValue(index);
				
				c.notify();
				
				return res;
			} catch (InterruptedException e) {
				L.e(getServiceClass(), "Failed to wait!");
				return null;
			}
		}

	}
	public void execute(Callable<Object> c) {
		getCallables().put(c, new Object());
	}
	
	private IndexedMap<Callable<Object>, Object> getCallables() {
		return callables;
	}
	
	protected void validateThread() {
		if(!Thread.currentThread().getName().equals(getServiceClass().getSimpleName())) {
			L.f(this.getClass(), new IllegalStateException( "You are not allowed to invoke the method directly. Use the execute() or executeAndWait()"
					+ " to run the method using the service's thread."));
		}
	}
}
