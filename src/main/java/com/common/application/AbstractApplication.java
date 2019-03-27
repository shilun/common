package com.common.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

public abstract class AbstractApplication  {
    protected void run(){
        SpringApplication app =new SpringApplication(buildRunClass());
        app.addListeners(buildListeners());
        app.run();
    }

    protected ApplicationListener[] buildListeners(){
        ApplicationListener[] listeners=new ApplicationListener[3];
        listeners[0]= new ApplicationStartingEventListener();
        listeners[1]=new ApplicationFailedEventListener();
        listeners[2]=new ApplicationStartedEventListener();
        return listeners;
    }

    protected abstract void starting();

    protected abstract void startFailed(Throwable throwable);

    protected abstract void started();
    protected abstract Class buildRunClass();


    class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {

        @Override
        public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
            startFailed(applicationFailedEvent.getException());
        }


    }
    class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {
        @Override
        public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
            starting();
        }
    }

    class ApplicationStartedEventListener implements ApplicationListener<ApplicationReadyEvent>{
        @Override
        public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            started();
        }
    }
}

