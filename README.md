# Schrödinger Simulator

A 1D quantum mechanics simulator built in Java using JavaFX.

## Features

* Time-dependent Schrödinger equation solver
* Crank–Nicolson integration
* Thomas algorithm for tridiagonal systems
* Harmonic oscillator potential
* Probability conservation
* Position expectation value
* Energy expectation value
* Real-time visualization with JavaFX

## Physics

The simulator solves:

iℏ ∂ψ/∂t = Hψ

using the Crank–Nicolson method:

(I + iHΔt/2)ψⁿ⁺¹ = (I - iHΔt/2)ψⁿ

Supports:

* Harmonic oscillator potentials
* Barrier potentials
* Wave packet evolution

## Tech Stack

* Java
* JavaFX

## Run

Compile:

javac --module-path "C:\javafx-sdk\lib" --add-modules javafx.controls src/*.java

Run:

java --enable-native-access=javafx.graphics --module-path "C:\javafx-sdk\lib" --add-modules javafx.controls -cp src App

## Future Improvements

* Momentum expectation
* Eigenstate solver
* Double slit simulation
* 2D quantum simulation
