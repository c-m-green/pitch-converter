# pitch-converter

## Overview

*pitch-converter* is a command-line Java application that encodes and decodes musical ciphers. This is my first personal coding project.

The goal of this project was to make this a viable tool for composers and other curious people to convert bits of text into music, which could be exported into useful file formats like MusicXML. The output files could then be imported into notation software or, in the case of MIDI (Issue https://github.com/c-m-green/pitch-converter/issues/2), simply listened to. The operation could then be reversed in order to "decode" musical phrases.

This project will be succeeded by [Read2Reed](https://github.com/c-m-green/Read2Reed) and [r2r-pitch-converter](https://github.com/c-m-green/r2r-pitch-converter), with the latter powering the former in a .NET desktop app.

Original blurb:

> Ever wanted to encode a message in music and then decipher it? Want to store snarky messages in your songs? Feel like taking a random bunch of numbers and seeing what they sound like?
>
> Here's an app I'm working on that lets you do just that!

## How to build

Requirements: JDK 17, Maven 3

```
mvn clean package
```

## Usage

`java -jar <app-name.jar> --mode=<encode|decode> <input-file> <output-file>`

(Use `-h` for full list of options.)

## Encoding

Two means of encoding are offered: *by letter* and *by degree*.

- by letter: A -> A, B -> B, C -> C, (...), G -> G, H -> A, I -> B, (etc.) where the letters of the alphabet are directly mapped to pitch class letters.
- by degree: A -> C, B -> D, C -> E, (...), G -> B, H -> C, I -> D, (etc.) where a letter's position in the alphabet is mapped to its corresponding scale degree in the C major scale.

Both have various flags that can be toggled on (or off). For example, encoding "by degree" can optionally utilize all twelve notes of the chromatic scale, thus mapping the index of each letter to its equivalent pitch in [integer notation](https://en.wikipedia.org/wiki/Pitch_class#Integer_notation).

## Decoding

Decoding is still very much a work in progress. Besides general code cleanup, the decoding functionality of *pitch-converter* needs some major work before it can be seriously used.

The app's current method of decoding is rudimentary and very naïve. It will explore all possible combinations of letters that could have yielded each pitch and try matching against a dictionary of English words. If rests were enabled, the app will try splitting a musical sequence based on the position of rests and attempting to decode each "word". Otherwise, it will attempt to find words in the entire sequence.

As the app decodes musical input, if it sucessfully interprets a combination of letters as a complete string of one or more words, it will add it to a list of possibilities. If the input is especially large, the list of possibilities can be enormous (assuming the input settings matched the settings used to encode).

I'd call it brute-forcing, but I'd say that this implies that the application has any definitive idea of what the correct answer is. As it currently stands, human eyes and minds are required to discern the correct message from a sea of "best guesses".

Other potential improvements include:
- Accounting for transposition
- The ability to read file formats other than a text file of `toString()` representations of the objects that hold notes and rests
