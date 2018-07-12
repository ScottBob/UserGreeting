package com.kcar;

import org.approvaltests.Approvals;
import org.junit.Test;

import java.util.Map;

public class DataAccessTest {
    @Test
    public void getDataTest() {
        DataAccess da = new DataAccess();
        Map<String, String> data = da.getData();
        Approvals.verify(data);
    }
}
