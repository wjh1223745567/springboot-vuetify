package com.iotinall.framework.modules.dl4j.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.MultiImageTransform;
import org.datavec.image.transform.ShowImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 识别猫
 */
@Slf4j
@Service
public class CatSearchService {

    private static int width=500, height = 500, channel = 2, bathSize=30;
    private static long seed = 500;

    private static int nEpochs = 100;

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 处理图片
     * @param dir
     */
    public void loadImageDir(File dir){
        File[] files = dir.listFiles();
        for (File file : files) {
            Mat mat = Imgcodecs.imread(file.getPath());
            Mat out = new Mat();
            Imgproc.cvtColor(mat, out, Imgproc.COLOR_BGR2GRAY, 0);

            Mat outSize = new Mat();
            Imgproc.resize(out, outSize, new Size(500,500));
            String name = "D:\\facematch\\cat\\whiteimg\\" + RandomStringUtils.randomAlphanumeric(10) + file.getName().substring(file.getName().lastIndexOf("."));
            System.out.println("输出文件：" + name);
            Imgcodecs.imwrite(name, outSize);
        }
    }

    public static void main(String[] args) {
        CatSearchService searchService = new CatSearchService();
//        searchService.loadImageDir(new File("D:\\facematch\\cat\\otherpic"));
        try {
//            DataSetIterator dataSetIterator = loadImage(new File("D:\\facematch\\cat\\whiteimg"));
//            System.out.println(dataSetIterator.batch());
            searchService.train();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 神经网络
     * @return
     */
    public MultiLayerConfiguration netConfiguration(){
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .l2(0.0005)
                .updater(new Adam(0.0001))
                .weightInit(WeightInit.XAVIER)
                .list()
                .layer(new ConvolutionLayer.Builder(5, 5)
                        //nIn and nOut specify depth. nIn here is the nChannels and nOut is the number of filters to be applied
                        .nIn(channel)
                        .stride(1,1)
                        .nOut(20)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2,2)
                        .stride(2,2)
                        .build())
                .layer(new ConvolutionLayer.Builder(5, 5)
                        //Note that nIn need not be specified in later layers
                        .stride(1,1)
                        .nOut(50)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2,2)
                        .stride(2,2)
                        .build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(2)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(500,500,2)) //See note below
                .build();
        return configuration;
    }

    public static DataSetIterator loadImage(File dir) throws IOException {

        FileSplit fileSplit = new FileSplit(dir, NativeImageLoader.ALLOWED_FORMATS, new Random(seed));
        ParentPathLabelGenerator generator = new ParentPathLabelGenerator();
        BalancedPathFilter pathFilter = new BalancedPathFilter(new Random(seed), generator, channel);
        InputSplit[] inputSplits = fileSplit.sample(pathFilter, 100);
        ImageRecordReader recordReader = new ImageRecordReader(height, width, channel, generator);
        recordReader.initialize(inputSplits[0], new MultiImageTransform(new Random(seed), new ShowImageTransform("图片处理")));
        DataSetIterator trainData = new RecordReaderDataSetIterator(recordReader, bathSize, channel, recordReader.numLabels());
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(trainData);
        trainData.setPreProcessor(scaler);
        return trainData;
    }


    public void train() throws IOException {
        MultiLayerNetwork net = new MultiLayerNetwork(netConfiguration());
        net.init();

        DataSetIterator trainIter = null;
        try {
            trainIter = loadImage(new File("D:\\facematch\\cat\\whiteimg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

// 训练的过程中同时进行评估
        for (int i = 0; i < nEpochs; i++) {
            net.fit(trainIter);
            log.info("Completed epoch " + i);
            Evaluation trainEval = net.evaluate(trainIter);
            log.info("train: " + trainEval.precision());
            trainIter.reset();
        }
//保存模型
        ModelSerializer.writeModel(net, new File( "D:\\facematch\\cat\\mouth-model.zip"), true);
    }
}
