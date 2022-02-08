package mx.kenzie.remix.parser;

import mx.kenzie.jupiter.reader.StreamReader;

import java.io.IOException;

public enum ParseMode {
    
    WORD {
        @Override
        public String read(StreamReader reader) throws IOException {
            return reader.readWord();
        }
    },
    DOT_WORD {
        @Override
        public String read(StreamReader reader) throws IOException {
            return (char) reader.read() + reader.readWord();
        }
    },
    ANY,
    STRING {
        @Override
        public String read(StreamReader reader) throws IOException {
            return (char) reader.read() + reader.readUntil('"', true);
        }
    };
    
    public String read(StreamReader reader) throws IOException {
        return reader.readUntilWhitespace();
    }
    
}
