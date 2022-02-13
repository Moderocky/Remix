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
            final StringBuilder builder = new StringBuilder();
            builder.append((char) reader.read());
            reader.readWhitespace();
            builder.append(reader.readWord());
            return builder.toString();
        }
    },
    DOLLAR_WORD {
        @Override
        public String read(StreamReader reader) throws IOException {
            final StringBuilder builder = new StringBuilder();
            builder.append((char) reader.read());
            reader.readWhitespace();
            builder.append(reader.readWord());
            return builder.toString();
        }
    },
    SYMBOL {
        @Override
        public String read(StreamReader reader) throws IOException {
            return "" + (char) reader.read();
        }
    },
    SYMBOL_2 {
        @Override
        public String read(StreamReader reader) throws IOException {
            return (char) reader.read() + "" + (char) reader.read();
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
