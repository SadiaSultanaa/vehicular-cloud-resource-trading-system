<#
PowerShell run script for Vehicular Cloud project.
Usage: ./run.ps1

This script sets up the classpath to target/classes and runs the main class.
#>

$mainClass = 'com.vehicularcloud.VehicularCloudApplication'
$outDir = 'target/classes'

if (-not (Test-Path $outDir)) {
    Write-Error "Compiled classes not found. Please run ./build.ps1 first."
    exit 1
}

Write-Host "Running $mainClass"

& java -cp $outDir $mainClass
