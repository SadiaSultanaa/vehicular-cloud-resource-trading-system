<#
PowerShell build script for the Vehicular Cloud project.
Usage: ./build.ps1

This script checks that Java is available and at least version 21, then
compiles Java sources in src/main/java to target/classes.
#>

Write-Host "Vehicular Cloud - Build script"

function Get-JavaMajorVersion {
    $v = & java -version 2>&1 | Select-String 'version' -SimpleMatch
    if (-not $v) { return $null }
    $m = $v -replace '.*"([0-9]+)(?:\..*)?".*','$1'
    return [int]$m
}

$javaVer = Get-JavaMajorVersion
if (-not $javaVer) {
    Write-Error "Java not found. Please install JDK 21 and set JAVA_HOME and PATH."
    exit 1
}

if ($javaVer -lt 21) {
    Write-Warning "Detected Java major version $javaVer. JDK 21 or later is recommended for this upgrade."
    Write-Host "You can continue, but compilation/run may fail if Java 21 APIs are required."
}

$sourceDir = "src/main/java"
$outDir = "target/classes"

if (-Not (Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir -Force | Out-Null }

# Find .java files
$files = Get-ChildItem -Path $sourceDir -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if (-not $files) {
    Write-Error "No Java source files found under $sourceDir"
    exit 1
}

Write-Host "Compiling $($files.Count) files..."

$javacArgs = @(
    '-d', $outDir
)

$javacArgs += $files

& javac @javacArgs
if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed (javac exit code $LASTEXITCODE)"
    exit $LASTEXITCODE
}

Write-Host "Compilation completed. Classes in $outDir"

Write-Host "To run: ./run.ps1" -ForegroundColor Green
