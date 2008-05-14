package org.orbisgis.pluginManager.background;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class JobQueue implements BackgroundManager {

	private static Logger logger = Logger.getLogger(JobQueue.class);

	private ArrayList<BackgroundListener> listeners = new ArrayList<BackgroundListener>();

	private ArrayList<Job> queue = new ArrayList<Job>();
	private Job current;

	private ProgressDialog dlg = new ProgressDialog();

	public synchronized void add(JobId processId, BackgroundJob lp,
			boolean blocking) {
		logger.info("Adding a job: " + processId);
		Job newJob = new Job(processId, lp, this, blocking);
		// Check if it's the current process
		if ((current != null) && (current.getId().is(processId))) {
			current.cancel();
			queue.add(0, newJob);
			fireJobAdded(newJob);
			newJob.progressTo(0);
			// we don't planify because we will do it when the cancelled process
			// ends
		} else {
			// Substitute existing process
			for (Job job : queue) {
				if (job.getId().is(processId)) {
					logger.info("Substituting previous job: " + processId);
					job.setProcess(lp);
					fireJobReplaced(job);
					return;
				}
			}

			// Add a new one
			logger.info("It's a new job: " + processId);
			queue.add(newJob);
			fireJobAdded(newJob);
			newJob.progressTo(0);

			planify();
		}
	}

	private synchronized void planify() {
		if (current == null && queue.size() > 0) {
			current = queue.remove(0);
			logger.info("Starting job: " + current.getId());

			if (current.isBlocking()) {
				dlg.setJob(current);
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						logger.info("Showing dialog for job: "
								+ current.getId());
						dlg.setVisible(true);
					}

				});
			} else {
				current.start();
			}
		}
	}

	public void add(BackgroundJob lp, boolean blocking) {
		add(new UniqueJobID(), lp, blocking);
	}

	public synchronized void processFinished(JobId processId) {
		logger.info("Job finished: " + processId);
		Job finishedJob = current;
		current = null;
		planify();
		fireJobRemoved(finishedJob);
		if (dlg.isVisible()) {
			dlg.setVisible(false);
		}
	}

	public synchronized Job[] getJobs() {
		Job[] jobs = queue.toArray(new Job[0]);
		if (current == null) {
			return jobs;
		} else {
			Job[] ret = new Job[jobs.length + 1];
			ret[0] = current;
			System.arraycopy(jobs, 0, ret, 1, jobs.length);

			return ret;
		}
	}

	public void backgroundOperation(BackgroundJob lp) {
		add(lp, true);
	}

	public void backgroundOperation(JobId processId, BackgroundJob lp) {
		add(processId, lp, true);
	}

	public void addBackgroundListener(BackgroundListener listener) {
		listeners.add(listener);
	}

	public void removeBackgroundListener(BackgroundListener listener) {
		listeners.remove(listener);
	}

	private void fireJobReplaced(Job job) {
		for (BackgroundListener listener : listeners) {
			listener.jobReplaced(job);
		}
	}

	private void fireJobRemoved(Job job) {
		for (BackgroundListener listener : listeners) {
			listener.jobRemoved(job);
		}
	}

	private void fireJobAdded(Job job) {
		for (BackgroundListener listener : listeners) {
			listener.jobAdded(job);
		}
	}

	public JobQueue getJobQueue() {
		return this;
	}

	public void nonBlockingBackgroundOperation(BackgroundJob lp) {
		add(lp, false);
	}

	public void nonBlockingBackgroundOperation(JobId processId,
			BackgroundJob lp) {
		add(processId, lp, false);
	}

}
