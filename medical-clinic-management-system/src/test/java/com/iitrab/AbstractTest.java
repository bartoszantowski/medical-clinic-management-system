package com.iitrab;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractTest {

    @Autowired
    private TestDataCleanUp testDataCleanUp;

    @BeforeEach
    public void setUp() {
        testDataCleanUp.cleanUp();
    }
}
