package com.portix.portix.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * ===========================================================
 * PORTIX - Resume Service
 * ===========================================================
 *
 * Responsibilities
 * ----------------
 * 1. Read uploaded resume files
 * 2. Extract resume text
 * 3. Validate resume files
 * 4. Provide resume content for further processing
 *
 * Future Responsibilities
 * -----------------------
 * - DOC/DOCX Support
 * - OCR Support
 * - Resume Versioning
 * - Resume Metadata
 * - Cloud Storage Integration
 *
 * NOTE:
 * -----
 * This service ONLY handles resume files.
 * It does NOT perform ATS analysis.
 * It does NOT parse candidate information.
 *
 * ===========================================================
 */

@Service
public class ResumeService {

    /**
     * Extracts text from a PDF resume.
     *
     * @param filePath Path of uploaded resume
     * @return Extracted resume text
     * @throws IOException if resume cannot be read
     */
    public String extractText(String filePath) throws IOException {

        validateResume(filePath);

        File resumeFile = new File(filePath);

        try (PDDocument document = Loader.loadPDF(resumeFile)) {

            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            String resumeText = pdfTextStripper.getText(document);

            System.out.println("Resume text extracted successfully.");

            return resumeText.trim();
        }
    }

    /**
     * Validates whether the uploaded resume exists.
     *
     * @param filePath Resume path
     * @throws IOException if file is missing
     */
    private void validateResume(String filePath) throws IOException {

        File resumeFile = new File(filePath);

        if (!resumeFile.exists()) {

            throw new IOException(
                    "Resume file not found : "
                            + resumeFile.getAbsolutePath()
            );
        }
    }


/**
 * Checks whether the uploaded file is a PDF.
 *
 * @param filePath Resume file path
 * @return true if PDF otherwise false
 */
public boolean isPdfFile(String filePath) {

    if (filePath == null || filePath.isBlank()) {
        return false;
    }

    return filePath.toLowerCase().endsWith(".pdf");
}

/**
 * Returns the uploaded resume file name.
 *
 * @param filePath Resume path
 * @return Resume file name
 */
public String getResumeFileName(String filePath) {

    File resumeFile = new File(filePath);

    return resumeFile.getName();
}

/**
 * Returns resume file size in KB.
 *
 * @param filePath Resume path
 * @return File size in KB
 */
public long getResumeSizeInKB(String filePath) {

    File resumeFile = new File(filePath);

    return resumeFile.length() / 1024;
}

/**
 * Checks whether resume file is empty.
 *
 * @param filePath Resume path
 * @return true if empty
 */
public boolean isResumeEmpty(String filePath) {

    File resumeFile = new File(filePath);

    return resumeFile.length() == 0;
}

/**
 * Deletes resume file.
 *
 * This method will be useful when
 * users replace an old resume.
 *
 * @param filePath Resume path
 * @return true if deleted successfully
 */
public boolean deleteResume(String filePath) {

    File resumeFile = new File(filePath);

    if (!resumeFile.exists()) {
        return false;
    }

    return resumeFile.delete();
}

/**
 * Checks whether the resume exists.
 *
 * @param filePath Resume path
 * @return true if present
 */
public boolean resumeExists(String filePath) {

    File resumeFile = new File(filePath);

    return resumeFile.exists();
}
/**
 * Returns the total number of words in the resume.
 *
 * @param resumeText Extracted resume text
 * @return Total words
 */
public int getWordCount(String resumeText) {

    if (resumeText == null || resumeText.isBlank()) {
        return 0;
    }

    return resumeText.trim().split("\\s+").length;
}

/**
 * Returns the total number of lines in the resume.
 *
 * @param resumeText Extracted resume text
 * @return Total lines
 */
public int getLineCount(String resumeText) {

    if (resumeText == null || resumeText.isBlank()) {
        return 0;
    }

    return resumeText.split("\\r?\\n").length;
}

/**
 * Checks whether extracted resume text is valid.
 *
 * @param resumeText Extracted text
 * @return true if valid
 */
public boolean hasExtractedText(String resumeText) {

    return resumeText != null
            && !resumeText.trim().isEmpty();
}

/**
 * Returns a short preview of the extracted resume.
 *
 * Useful for dashboard preview.
 *
 * @param resumeText Resume text
 * @return Preview text
 */
public String getResumePreview(String resumeText) {

    if (!hasExtractedText(resumeText)) {
        return "";
    }

    if (resumeText.length() <= 300) {
        return resumeText;
    }

    return resumeText.substring(0, 300) + "...";
}

/**
 * Future Feature
 * --------------
 * DOCX resume extraction.
 *
 * This will be implemented in a future sprint.
 */
public String extractDocxText(String filePath) {

    throw new UnsupportedOperationException(
            "DOCX support will be implemented in a future release."
    );
}

/**
 * Future Feature
 * --------------
 * Image resume OCR.
 *
 * This will be implemented in a future sprint.
 */
public String extractImageText(String filePath) {

    throw new UnsupportedOperationException(
            "OCR support will be implemented in a future release."
    );
}


}