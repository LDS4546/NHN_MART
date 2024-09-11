/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.nhnmart.entring;

import com.nhnacademy.customer.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.function.Try;
import org.junit.platform.commons.util.ReflectionUtils;

@Slf4j
class EnteringQueueTest {
    EnteringQueue enteringQueue;
    @BeforeEach
    void setUp() {
        /*TODO#3-9
            Customer{id=1, name='NHN아카데미1', money=1000000}
            ~
            Customer{id=99, name='NHN아카데미99', money=1000000}
            1~99 고객을 생성 후 enteringQueue 대기열에 등록 합니다.
         */
        enteringQueue = new EnteringQueue();
        for(int i=1; i< 100; i++){
            enteringQueue.addCustomer(new Customer(i, "NHN아카데미1", 1000000));
        }

    }

    @Test
    @DisplayName("default queue capacity = 100")
    void constructorTest_InitCapacity() throws Exception {
        Try<Object> capacity = ReflectionUtils.tryToReadFieldValue(EnteringQueue.class,"capacity",enteringQueue);
        Assertions.assertEquals(100, (int)capacity.get() );
    }

    @Test
    void addCustomer() throws Exception {
        //TODO#3-10 id=100인 고객을 enteringQueue에 등록하고 검증 합니다.
        enteringQueue.addCustomer(new Customer(100l, "NHN아카데미100",100_0000));
        Assertions.assertEquals(100, enteringQueue.getQueueSize());

    }

    @Test
    @DisplayName("queue - poll test")
    void getCustomer() {
        //TODO#3-11  enteringQueue에서 enteringQueue.getCustomer() 호출시 반환되는 값을 검증 합니다.
        Assertions.assertEquals(1, enteringQueue.getCustomer().getId());

    }

    @Test
    @DisplayName("blocking queue test : queue size : 100, 101번째 customer를 추가 한다면, consumer에 의해서 소비될 때 까지 대기 합니다.")
    void blockingTest() throws Exception {

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                Customer customer100= new Customer(100l, "NHN아카데미100",100_0000);
                log.debug("2초 대기 후 101-customer 추가 됨");
                Customer customer101= new Customer(101l, "NHN아카데미101",100_0000);
                enteringQueue.addCustomer(customer100);
                enteringQueue.addCustomer(customer101);
            }
        });
        producer.start();

        //TODO#3-12 2초 대기후 enteringQueue.getCustomer() 호출해서 소비할 수 있도록 consumer Thread를 구현 합니다.
        Thread consumer =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                enteringQueue.getCustomer();
            }
        });
        consumer.start();

        log.debug("{}", producer.getState());
        //TODO#3-13  producer or consumer 실행 중이라면 대기 합니다. yield()를 이용해서 구현하세요.
        while(producer.isAlive() || consumer.isAlive()){
            Thread.yield();
        }


        int actual = enteringQueue.getQueueSize();
        Assertions.assertEquals(100,actual);
    }
}