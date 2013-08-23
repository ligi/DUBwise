package org.ligi;


import org.junit.runner.RunWith;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.helper.DUBwiseStringHelper;
import org.ligi.ufo.MKParamsGeneratedDefinitions;
import org.ligi.ufo.MKParamsParser;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.fail;

@RunWith(RobolectricTestRunner.class)
public class TheJMKIntegration {


    @org.junit.Test
    public void shouldHaveStringsForAllParamsInDefault() throws Exception {

        MKProvider.getMK().params.set_by_mk_data(MKParamsParser.default_params[0]);
        int[][] field_stringids = MKProvider.getMK().params.field_stringids;

        for (int[] arr1 : field_stringids) {
            for (int stringId : arr1) {
                if (stringId > DUBwiseStringHelper.table.length) {
                    fail("DUBwiseStringHelper has no String for " + stringId);
                }
            }
        }

    }

    @org.junit.Test
    public void shouldHaveStringsForAllParamsInGenerated() throws Exception {
        for (int[][] arrOutest : MKParamsGeneratedDefinitions.all_field_stringids) {
            for (int[] arrMiddle : arrOutest) {
                for (int stringId : arrMiddle) {
                    if (stringId > DUBwiseStringHelper.table.length) {
                        fail("DUBwiseStringHelper has no String for " + stringId);
                    }
                }
            }

        }
    }
}