package com.gujs.base.pdfToTxt

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import java.io.File

object pdfToTxt {
  def convertPdfToTxt(inputPath: String, outputPath: String): Unit = {
    val file = new File(inputPath)
    val document = PDDocument.load(file)
    val stripper = new PDFTextStripper()

    val text = stripper.getText(document)

    val outputFile = new File(outputPath)
    val writer = new java.io.PrintWriter(outputFile)
    writer.print(text)
    writer.close()

    document.close()

    println("PDF转换为TXT成功!")
  }

  def main(args: Array[String]): Unit = {
    val inputPath = "xtgc.pdf"
    val outputPath = "xtgc.txt"
    convertPdfToTxt(inputPath, outputPath)
  }
}