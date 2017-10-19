package com.dhis2.usescases.general;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Created by ppajuelo on 27/09/2017.
 */

public class AbstractActivityContracts {

    public interface View {
        Context getContext();

        ActivityGlobalAbstract getAbstracContext();

        void startActivity(@NonNull Class<?> destination, @Nullable Bundle bundle, boolean finishCurrent, boolean finishAll, @Nullable ActivityOptionsCompat transition);
    }


}
