//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.worker;

import com.common.exception.ApplicationException;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import java.lang.reflect.Method;
import java.util.*;

public class WorkService implements ApplicationContextAware {
    protected static final Logger LOGGER = LoggerFactory.getLogger(WorkService.class.getName());
    private ApplicationContext applicationContext;

    public WorkService() {
    }

    public List<WorkerSchedulerBean> getListWorker() {
        ArrayList<WorkerSchedulerBean> beanList = new ArrayList();
        Map beansOfType = this.applicationContext.getBeansOfType(WorkerSchedulerBean.class);
        Iterator var3 = beansOfType.keySet().iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            WorkerSchedulerBean bean = (WorkerSchedulerBean) beansOfType.get(key);
            if (!beanList.contains(bean)) {
                beanList.add(bean);
            }
        }
        Collections.sort(beanList, new Comparator<WorkerSchedulerBean>() {
            public int compare(WorkerSchedulerBean o1, WorkerSchedulerBean o2) {
                return o1.getGroupIndex() == o2.getGroupIndex() ? o1.getIndex().compareTo(o2.getIndex()) : o1.getGroupIndex().compareTo(o2.getGroupIndex());
            }
        });
        return beanList;
    }


    public void stopOrStart(String id) {
        List listWorker = this.getListWorker();
        Iterator var3 = listWorker.iterator();

        while (var3.hasNext()) {
            WorkerSchedulerBean worker = (WorkerSchedulerBean) var3.next();
            if (worker.getId().equals(id)) {
                if (worker.isRunning()) {
                    worker.stop();
                } else {
                    worker.start();
                }
            }
        }

    }

    public void doStop(String id) {
        List listWorker = this.getListWorker();
        Iterator var3 = listWorker.iterator();

        while (var3.hasNext()) {
            final WorkerSchedulerBean worker = (WorkerSchedulerBean) var3.next();
            if (worker.getId().equals(id)) {
                Thread var10000 = new Thread() {
                    public void run() {
                        worker.stop();
                    }
                };
            }
        }

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void doExecute(String id) {
        try {
            List e = this.getListWorker();
            Iterator var3 = e.iterator();

            WorkerSchedulerBean bean;
            do {
                if (!var3.hasNext()) {
                    return;
                }
                bean = (WorkerSchedulerBean) var3.next();
            } while (!bean.getId().equals(id));

            JobDetail jobDetail = null;
            MethodInvokingJobDetailFactoryBean b = null;
            Method method = null;
            Scheduler scheduler = bean.getScheduler();
            List jobGroupNames = scheduler.getJobGroupNames();
            Set keys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals("DEFAULT"));
            JobKey jobKey = null;

            for (Iterator iterator = keys.iterator(); iterator.hasNext(); jobKey = (JobKey) iterator.next()) {
                ;
            }

            jobDetail = scheduler.getJobDetail(jobKey);
            b = (MethodInvokingJobDetailFactoryBean) jobDetail.getJobDataMap().get("methodInvoker");
            method = b.getPreparedMethod();
            method.invoke(b.getTargetObject(), new Object[0]);
        } catch (Exception var13) {
            throw new ApplicationException("do task error");
        }
    }
}
