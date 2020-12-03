import java.io.*;
import java.util.ArrayList;
class main {
    public static void main(String[] args) throws IOException {
        //In powershell, to escape a space char, you need to do "` "
        //You can add whatever arguments to a powershell script via -name ect...
        var out = CommandCenter.executePowerShell("C:\\Users\\Pepin\\OneDrive\\Side` Hustle\\PowerShell\\src\\file.ps1 -name Henry -age 34 -sex male");
        //var out = CommandCenter.executePowerShell("Get-Process");
        for (String el : out) {
            System.out.println(el);
        }
    }
}

public final class CommandCenter {
    //Not really sure from what context this is running, if you put in notepad.exe or powershell it'll launch it
    //Doesn't take DIR or ls, so not CMD or powershell
    public static Process runCommand(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static ArrayList<String> executePowerShell(String command) throws IOException {
        Process powerShellProcess = runCommand("powershell " + command);
        //Prevents user ipnput
        powerShellProcess.getOutputStream().close();

        var outputArray = new ArrayList<String>();
        
        String currentLine;

        BufferedReader stdout = new BufferedReader((new InputStreamReader(powerShellProcess.getInputStream())));
        while ((currentLine  = stdout.readLine()) != null) {
            outputArray.add(currentLine);
        }
        stdout.close();

        BufferedReader stderr = new BufferedReader((new InputStreamReader(powerShellProcess.getErrorStream())));
        while ((currentLine  = stderr.readLine()) != null) {
            outputArray.add(currentLine);
        }
        stderr.close();

        return outputArray;
    }
}
