/*
Copyright 2023 Arete

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package it.aretesoftware.gdx.jvdf;

/**
 * Parses VDF documents into iterable tree structures.
 * @author Brendan Heinonen */
public class GdxVDFParser {

    private final GdxVDFPreprocessor preprocessor;

    /**
     * Initializes the VDFParser with a specific preprocessor
     * @param preprocessor the preprocessor to process input strings with
     */
    public GdxVDFParser(GdxVDFPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    /**
     * Initializes the VDFParser with the default preprocessor (VDFPreprocessor)
     */
    public GdxVDFParser() {
        this(new GdxVDFPreprocessor());
    }

    /**
     * Parses a VDF document.
     * @param vdf the VDF document to parse
     * @return a VDFNode which represents the root of the VDF document
     */
    public GdxVDFNode parse(String vdf) {
        return parse(vdf.split("\\n"));
    }

    /**
     * Parses a VDF document
     * @param vdf an array of lines representing a VDF document to parse
     * @return a VDFNode which represents the node of the VDF document
     */
    public GdxVDFNode parse(String[] vdf) {
        String processed = preprocessor.process(vdf);
        GdxVDFParserState state = new GdxVDFParserState();

        char[] arr = processed.toCharArray();
        for(char c : arr) {
            switch (c) {
                case '"':
                    state.quote();
                    break;
                case ' ':
                    state.space();
                    break;
                case '\\':
                    state.escape();
                    break;
                case '{':
                    state.beginSubNode();
                    break;
                case '}':
                    state.endSubNode();
                    break;
                default:
                    state.character(c);
                    break;
            }
        }
        state.endParse();

        return state.root();
    }


}
