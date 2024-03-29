/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.holo.app;

import android.app.KeyguardManager;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class CaptureHolo extends ActivityInstrumentationTestCase2<CaptureActivity> {

    public CaptureHolo() {
        super(CaptureActivity.class);
    }

    public void testCaptureHolo() throws Exception {
        setActivityInitialTouchMode(true);
        CaptureActivity activity = getActivity();
        KeyguardManager keyguardManager =
                (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.newKeyguardLock("holo_capture").disableKeyguard();
        activity.waitForCompletion();
    }
}
