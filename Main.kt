import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    println(execPS("get-process"))
}

fun execPS(input:String):String {
    val command = "powershell.exe $input"
    val powerShellProcess = Runtime.getRuntime().exec(command)
    powerShellProcess.outputStream.close()

    var line: String?

    var output = ""
    //Runs command line by line until and null which could imply an error or null value
    val stdout = BufferedReader(InputStreamReader(powerShellProcess.inputStream))
    while (stdout.readLine().also { line = it } != null) {
        output = "$output$line\n"
    }
    stdout.close()

    //
    val stderr = BufferedReader(InputStreamReader(powerShellProcess.errorStream))
    while (stderr.readLine().also { line = it } != null) {
        output = "$output$line\n"
    }
    stderr.close()

    return output;
}

