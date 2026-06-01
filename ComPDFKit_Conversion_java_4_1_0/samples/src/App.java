import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.compdfkit.conversion.CPDFConversion;
import com.compdfkit.conversion.LibraryManager;
import com.compdfkit.conversion.base.ErrorCode;
import com.compdfkit.conversion.base.ExcelOptions;
import com.compdfkit.conversion.base.HtmlOptions;
import com.compdfkit.conversion.base.ImageOptions;
import com.compdfkit.conversion.base.JsonOptions;
import com.compdfkit.conversion.base.MarkdownOptions;
import com.compdfkit.conversion.base.OfdOptions;
import com.compdfkit.conversion.base.OCRLanguage;
import com.compdfkit.conversion.base.PptOptions;
import com.compdfkit.conversion.base.ConvertCallback;
import com.compdfkit.conversion.base.RtfOptions;
import com.compdfkit.conversion.base.SearchablePdfOptions;
import com.compdfkit.conversion.base.TxtOptions;
import com.compdfkit.conversion.base.WordOptions;

class ProgressClass implements ConvertCallback {
    private int lastProgress = 0;
    @Override
    public void onProgress(int current, int total) {
        lastProgress = current;
        System.out.printf("Current Progress: %d / %d%n", current, total);
    }
    @Override
    public boolean isCancelled() {
        return false;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        String pwd = args.length > 0 ? args[0] : System.getProperty("user.dir");

        Path basePath = Paths.get(pwd).toAbsolutePath().normalize();
        Path licensePath = basePath.resolve("license.xml");
        ErrorCode errorCode = LibraryManager.licenseVerify(licensePath.toString());
        
        if (errorCode != ErrorCode.SUCCESS) {
            return;
        }

        Path pdfPath = basePath.resolve("pdf");
        Path excelTestFilePath = pdfPath.resolve("excel.pdf");
        Path pptTestFilePath = pdfPath.resolve("powerpoint.pdf");
        Path wordTestFilePath = pdfPath.resolve("word.pdf");
        Path wordWithOCRTestFilePath = pdfPath.resolve("word.pdf");

        Path resourcePath = basePath.getParent().resolve("resource");
        Path modelPath = resourcePath.resolve("models").resolve("documentai.model");
        Path outputPath = basePath.resolve("output");

        LibraryManager.getVersion();

        LibraryManager.initialize(resourcePath.toString());
        LibraryManager.setDocumentAIModel(modelPath.toString());

        // pdf to word
        WordOptions wordOpt = new WordOptions();
        CPDFConversion.startPDFToWord(wordTestFilePath.toString(), "", outputPath.toString(), wordOpt, new ProgressClass());

        // pdf to excel
        ExcelOptions excelOpt = new ExcelOptions();
        CPDFConversion.startPDFToExcel(excelTestFilePath.toString(), "", outputPath.toString(), excelOpt, new ProgressClass());

        // pdf to ppt
        PptOptions pptOpt = new PptOptions();
        CPDFConversion.startPDFToPpt(pptTestFilePath.toString(), "", outputPath.toString(), pptOpt, new ProgressClass());

        // pdf to csv
        excelOpt.setCsvFormat(true);
        CPDFConversion.startPDFToExcel(excelTestFilePath.toString(), "", outputPath.toString(), excelOpt, new ProgressClass());

        // pdf to html
        HtmlOptions htmlOpt = new HtmlOptions();
        CPDFConversion.startPDFToHtml(wordTestFilePath.toString(), "", outputPath.toString(), htmlOpt, new ProgressClass());

        // pdf to rtf
        RtfOptions rtfOpt = new RtfOptions();
        CPDFConversion.startPDFToRtf(wordTestFilePath.toString(), "", outputPath.toString(), rtfOpt, new ProgressClass());

        // pdf to image
        CPDFConversion.startPDFToImage(wordTestFilePath.toString(), "", outputPath.toString(), new ImageOptions(), new ProgressClass());

        // pdf to txt
        TxtOptions txtOpt = new TxtOptions();
        CPDFConversion.startPDFToTxt(wordTestFilePath.toString(), "", outputPath.toString(), txtOpt, new ProgressClass());

        // pdf to json
        JsonOptions jsonOpt = new JsonOptions();
        CPDFConversion.startPDFToJson(wordTestFilePath.toString(), "", outputPath.toString(), jsonOpt, new ProgressClass());
        
        // pdf to markdown
        MarkdownOptions mdOpt = new MarkdownOptions();
        CPDFConversion.startPDFToMarkdown(wordTestFilePath.toString(), "", outputPath.toString(), mdOpt, new ProgressClass());

        // pdf to searchable pdf
        SearchablePdfOptions opt = new SearchablePdfOptions();
        opt.setEnableOcr(true);
        opt.setLanguages(Arrays.asList(OCRLanguage.ENGLISH));
        CPDFConversion.startPDFToSearchablePdf(wordWithOCRTestFilePath.toString(), "", outputPath.toString(), opt, new ProgressClass());

        // pdf to ofd
        OfdOptions ofdOpt = new OfdOptions();
        ofdOpt.setEnableOcr(true);
        ofdOpt.setLanguages(Arrays.asList(OCRLanguage.ENGLISH));
        CPDFConversion.startPDFToOfd(wordWithOCRTestFilePath.toString(), "", outputPath.resolve("output.ofd").toString(), ofdOpt, new ProgressClass());

        LibraryManager.release();
    }
}

