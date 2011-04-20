package org.textway.tools.converter

import groovy.io.FileType

class Converter {




    public static void main(String[] args) {
        if(args.length >= 1) {
            println("converter - textway grammar converter");
            println("run in the directory with .spec files");
            System.exit(1);
        }

        def filesmap = [:]
        new File(".").eachFileMatch(~/.*_(lexer|parser)\.spec/) { String name = it.getName(); filesmap[name.substring(0, name.lastIndexOf('_'))] = it; }

        println "found: " + filesmap.collect {it}.join(", ")
    }
}
