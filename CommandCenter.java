import java.io.*;

import kotlin.jvm.internal.Intrinsics;

class main {
    public static void main(String[] args) throws IOException {
        //In powershell, to escape a space char, you need to do "` "
        //You can add whatever arguments to a powershell script via -name ect...
        var out = CommandCenter.executePowerShell("C:\\Users\\Pepin\\OneDrive\\Side` Hustle\\PowerShell\\src\\file.ps1 -name Henry -age 34 -sex male");
        System.out.println(out);
        CommandCenter.runCommand("notepad.exe");
    }
}

public final class CommandCenter {
    //Not really sure from what context this is running, if you put in notepad.exe or powershell it'll launch it
    //Doesn't take DIR or ls, so not CMD or powershell
    public static Process runCommand(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static String executePowerShell(String command) throws IOException {
        //Formats the command
        Process powerShellProcess = runCommand("powershell " + command);

        //Disables input, and Read-Host will be treated as null
        Intrinsics.checkNotNullExpressionValue(powerShellProcess, "powerShellProcess");
        powerShellProcess.getOutputStream().close();

        //Pretty sure this is writing out to powershell
        BufferedReader stdout = new BufferedReader((new InputStreamReader(powerShellProcess.getInputStream())));

        StringBuilder output = new StringBuilder();

        while (true) {
            //Pipe the line into powershell and store the return
            String currentLine = stdout.readLine();

            //If the return is null then assume an error or end of file and stop running the script
            if (currentLine == null) {
                stdout.close();
                BufferedReader stderr = new BufferedReader((new InputStreamReader(powerShellProcess.getErrorStream())));

                //Outputs the error
                while (true) {
                    String currentErrorLine = stderr.readLine();
                    //If there is no more error code to print, then exit the function
                    if (currentErrorLine == null) {
                        stderr.close();
                        return output.toString();
                    }
                    //Store the error code
                    output.append(currentErrorLine);
                    output.append("\n");
                }
            }
            //If there is a valid return then store the return
            //
            //This is where you can do a lot of fancy things, like if you have a list
            //that is printing and you know the order, you can pipe the output into string,
            //Split the string via ' ' and then pipe those values into an object
            output.append(currentLine);
            output.append("\n");
        }
    }
}