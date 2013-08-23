package org.ligi;


import org.junit.runner.RunWith;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.helper.DUBwiseStringHelper;
import org.ligi.ufo.MKParamsGeneratedDefinitions;
import org.ligi.ufo.MKParamsGeneratedDefinitionsToStrings;
import org.ligi.ufo.MKParamsParser;

import static org.fest.assertions.api.Assertions.fail;


@RunWith(RobolectricGradleTestRunner.class)
public class TheJMKIntegrationTest {

    @org.junit.Test
    public void shouldHaveStringsForAllParamsInGenerated() throws Exception {
        for (int[][] arrOutest : MKParamsGeneratedDefinitions.all_field_stringids) {
            for (int[] arrMiddle : arrOutest) {
                for (int paramId : arrMiddle) {
                    if (paramId > MKParamsGeneratedDefinitionsToStrings.PARAMID2STRINGID.length) {
                        fail("no string  mapping in MKParamsGeneratedDefinitionsToStrings for param " + paramId);
                    }
                }
            }

        }
    }
}