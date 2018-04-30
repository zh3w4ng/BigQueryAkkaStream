package io.diablogato

object Main {
  def main(args: Array[String]): Unit = {
    val config = BqQueryConfig.load match {
      case Left(s) => {
        println(s)
        return
      }
      case Right(v) => v
    }
    val (fields, rowIterator) = BqQueryRunner(config).run
    val rawPath = "out/raw.csv"
    val countPath = "out/counts.txt"
    Writer(fields, rowIterator, rawPath, countPath).write
  }
}
