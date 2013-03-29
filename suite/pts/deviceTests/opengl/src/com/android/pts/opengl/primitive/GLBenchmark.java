/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.android.pts.opengl.primitive;

import com.android.pts.util.PtsActivityInstrumentationTestCase2;
import com.android.pts.util.ResultType;
import com.android.pts.util.ResultUnit;

import android.content.Intent;
import android.cts.util.TimeoutReq;
import android.opengl.Matrix;

import java.util.Arrays;

/**
 * Runs the Primitive OpenGL ES 2.0 Benchmarks.
 */
public class GLBenchmark extends PtsActivityInstrumentationTestCase2<GLActivity> {

    private static final int NUM_FRAMES = 100;
    private static final int NUM_ITERATIONS = 8;
    private static final int TIME_OUT = 1000000;

    public GLBenchmark() {
        super(GLActivity.class);
    }

    /**
     * Runs the full OpenGL ES 2.0 pipeline test offscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testFullPipelineOffscreen() throws Exception {
        runBenchmark(Benchmark.FullPipeline, true, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the full OpenGL ES 2.0 pipeline test onscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testFullPipelineOnscreen() throws Exception {
        runBenchmark(Benchmark.FullPipeline, false, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the pixel output test offscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testPixelOutputOffscreen() throws Exception {
        runBenchmark(Benchmark.PixelOutput, true, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the pixel output test onscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testPixelOutputOnscreen() throws Exception {
        runBenchmark(Benchmark.PixelOutput, false, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the shader performance test offscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testShaderPerfOffscreen() throws Exception {
        runBenchmark(Benchmark.ShaderPerf, true, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the shader performance test onscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testShaderPerfOnscreen() throws Exception {
        runBenchmark(Benchmark.ShaderPerf, false, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the context switch overhead test offscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testContextSwitchOffscreen() throws Exception {
        runBenchmark(Benchmark.ContextSwitch, true, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the context switch overhead test onscreen.
     */
    @TimeoutReq(minutes = 100)
    public void testContextSwitchOnscreen() throws Exception {
        runBenchmark(Benchmark.ContextSwitch, false, NUM_FRAMES, NUM_ITERATIONS, TIME_OUT);
    }

    /**
     * Runs the specified test.
     *
     * @param benchmark An enum representing the benchmark to run.
     * @param offscreen Whether to render to an offscreen framebuffer rather than the screen.
     * @param numFrames The number of frames to render.
     * @param numIterations The number of iterations to run, each iteration has a bigger workload.
     * @param timeout The milliseconds to wait for an iteration of the benchmark before timing out.
     * @throws Exception If the benchmark could not be run.
     */
    private void runBenchmark(
            Benchmark benchmark, boolean offscreen, int numFrames, int numIterations, int timeout)
            throws Exception {
        String benchmarkName = benchmark.toString();
        Intent intent = new Intent();
        intent.putExtra(GLActivity.INTENT_EXTRA_BENCHMARK_NAME, benchmarkName);
        intent.putExtra(GLActivity.INTENT_EXTRA_OFFSCREEN, offscreen);
        intent.putExtra(GLActivity.INTENT_EXTRA_NUM_FRAMES, numFrames);
        intent.putExtra(GLActivity.INTENT_EXTRA_NUM_ITERATIONS, numIterations);
        intent.putExtra(GLActivity.INTENT_EXTRA_TIMEOUT, timeout);

        GLActivity activity = null;
        setActivityIntent(intent);
        try {
            activity = getActivity();
            activity.spawnAndWaitForCompletion();
        } finally {
            if (activity != null) {
                double[] fpsValues = activity.mFpsValues;
                double score = 0;
                for (double d : fpsValues) {
                    score += d;
                }
                score /= numIterations;// Average.

                getReportLog().printArray(
                        "Fps Values", fpsValues, ResultType.HIGHER_BETTER, ResultUnit.FPS);
                getReportLog()
                        .printSummary("Score", score, ResultType.HIGHER_BETTER, ResultUnit.SCORE);
                activity.finish();
            }
        }
    }
}
