/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.spring.timer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimerFlowTest {

    private static final Logger log = LoggerFactory.getLogger(TimerFlowTest.class);
    private static final String TMPDIR = System.getProperty("java.io.tmpdir");

    private ClassPathXmlApplicationContext ctx;

    @Before
    public void createSpringContext() {
        try {
            log.info("creating spring context");
            PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
            Properties properties = new Properties();
            properties.setProperty("temp.dir",
                    TMPDIR);
            configurer.setProperties(properties);
            ctx = new ClassPathXmlApplicationContext();
            ctx.addBeanFactoryPostProcessor(configurer);
            ctx.setConfigLocation("org/kie/spring/timer/conf/spring-conf.xml");
            ctx.refresh();
        } catch (Exception e) {
            log.error("can't create spring context",
                    e);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Ignore // test randomly fails on some computer architectures.
    public void doTest() throws Exception {
        //  do not use Thread.sleep() in MyDroolsBean, but use Object.wait() and Object.notifyAll() or a Latch instead

        MyDroolsBean myDroolsBean = (MyDroolsBean) ctx.getBean("myDroolsBean");

        assertEquals(0, myDroolsBean.getTimerTriggerCount());

        myDroolsBean.initStartDisposeAndLoadSession();

        int n = myDroolsBean.getTimerTriggerCount();
        assertTrue(n > 0);

        myDroolsBean.endTheProcess();
        assertTrue(myDroolsBean.getTimerTriggerCount() > n);
    }
}
