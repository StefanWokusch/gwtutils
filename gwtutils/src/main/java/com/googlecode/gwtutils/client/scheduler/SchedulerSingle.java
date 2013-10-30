package com.googlecode.gwtutils.client.scheduler;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/**
 * For refreshing things, sometimes its necessary to call the refresh very often. This causes many Performance problems.
 * 
 * The schedule method of this Class will call an GWT-Scheduler only when the last Schedule has been executed.
 * 
 * So you can call it on any position, you think, you need an ui-refresh, and it doesn't matter how often you call this, it will be executed only one time.
 */
public class SchedulerSingle {
	public static enum ScheduleType {
		/**
		 * @see Scheduler.scheduleFinally
		 */
		FINALLY {
			@Override
			protected void schedule(ScheduledCommand cmd) {
				Scheduler.get().scheduleFinally(cmd);
			}
		},
		/**
		 * @see Scheduler.scheduleEntry
		 */
		ENTRY {
			@Override
			protected void schedule(ScheduledCommand cmd) {
				Scheduler.get().scheduleEntry(cmd);
			}
		},
		/**
		 * @see Scheduler.scheduleDeferred
		 */
		DEFFERED {
			@Override
			protected void schedule(ScheduledCommand cmd) {
				Scheduler.get().scheduleDeferred(cmd);
			}
		};

		protected abstract void schedule(ScheduledCommand cmd);
	}

	private final ScheduledCommand cmd;
	private final ScheduleType type;

	private boolean scheduled = false;

	public SchedulerSingle(ScheduleType type, ScheduledCommand cmd) {
		this.cmd = cmd;
		this.type = type;
	}

	private final ScheduledCommand scheduleCommand = new ScheduledCommand() {
		@Override
		public void execute() {
			scheduled = false;
			cmd.execute();
		}
	};

	/**
	 * schedule the command if its not sheduled yet.
	 */
	public void schedule() {
		if (!scheduled) {
			scheduled = true;
			type.schedule(scheduleCommand);
		}
	}

}
