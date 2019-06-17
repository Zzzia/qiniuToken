//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package util;

import java.io.UnsupportedEncodingException;

public final class Base64 {
    public static final int DEFAULT = 0;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int CRLF = 4;
    public static final int URL_SAFE = 8;
    public static final int NO_CLOSE = 16;

    private Base64() {
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Base64.Decoder decoder = new Base64.Decoder(flags, new byte[len * 3 / 4]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (decoder.op == decoder.output.length) {
            return decoder.output;
        } else {
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
            return temp;
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException var3) {
            throw new AssertionError(var3);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "US-ASCII");
        } catch (UnsupportedEncodingException var5) {
            throw new AssertionError(var5);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Base64.Encoder encoder = new Base64.Encoder(flags, (byte[])null);
        int output_len = len / 3 * 4;
        if (encoder.do_padding) {
            if (len % 3 > 0) {
                output_len += 4;
            }
        } else {
            switch(len % 3) {
                case 0:
                default:
                    break;
                case 1:
                    output_len += 2;
                    break;
                case 2:
                    output_len += 3;
            }
        }

        if (encoder.do_newline && len > 0) {
            output_len += ((len - 1) / 57 + 1) * (encoder.do_cr ? 2 : 1);
        }

        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);

        assert encoder.op == output_len;

        return encoder.output;
    }

    static class Encoder extends Base64.Coder {
        public static final int LINE_GROUPS = 19;
        private static final byte[] ENCODE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        private static final byte[] ENCODE_WEBSAFE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        public final boolean do_padding;
        public final boolean do_newline;
        public final boolean do_cr;
        private final byte[] tail;
        private final byte[] alphabet;
        int tailLen;
        private int count;

        public Encoder(int flags, byte[] output) {
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            this.do_newline = (flags & 2) == 0;
            this.do_cr = (flags & 4) != 0;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        public int maxOutputSize(int len) {
            return len * 8 / 5 + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            byte[] alphabet = this.alphabet;
            byte[] output = this.output;
            int op = 0;
            int count = this.count;
            int p = offset;
            len += offset;
            int v = -1;
            int var10000;
            switch(this.tailLen) {
                case 0:
                default:
                    break;
                case 1:
                    if (offset + 2 <= len) {
                        var10000 = (this.tail[0] & 255) << 16;
                        p = offset + 1;
                        v = var10000 | (input[offset] & 255) << 8 | input[p++] & 255;
                        this.tailLen = 0;
                    }
                    break;
                case 2:
                    if (offset + 1 <= len) {
                        var10000 = (this.tail[0] & 255) << 16 | (this.tail[1] & 255) << 8;
                        p = offset + 1;
                        v = var10000 | input[offset] & 255;
                        this.tailLen = 0;
                    }
            }

            if (v != -1) {
                output[op++] = alphabet[v >> 18 & 63];
                output[op++] = alphabet[v >> 12 & 63];
                output[op++] = alphabet[v >> 6 & 63];
                output[op++] = alphabet[v & 63];
                --count;
                if (count == 0) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }

                    output[op++] = 10;
                    count = 19;
                }
            }

            while(p + 3 <= len) {
                v = (input[p] & 255) << 16 | (input[p + 1] & 255) << 8 | input[p + 2] & 255;
                output[op] = alphabet[v >> 18 & 63];
                output[op + 1] = alphabet[v >> 12 & 63];
                output[op + 2] = alphabet[v >> 6 & 63];
                output[op + 3] = alphabet[v & 63];
                p += 3;
                op += 4;
                --count;
                if (count == 0) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }

                    output[op++] = 10;
                    count = 19;
                }
            }

            if (finish) {
                int t;
                if (p - this.tailLen == len - 1) {
                    t = 0;
                    v = ((this.tailLen > 0 ? this.tail[t++] : input[p++]) & 255) << 4;
                    this.tailLen -= t;
                    output[op++] = alphabet[v >> 6 & 63];
                    output[op++] = alphabet[v & 63];
                    if (this.do_padding) {
                        output[op++] = 61;
                        output[op++] = 61;
                    }

                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op++] = 13;
                        }

                        output[op++] = 10;
                    }
                } else if (p - this.tailLen == len - 2) {
                    t = 0;
                    v = ((this.tailLen > 1 ? this.tail[t++] : input[p++]) & 255) << 10 | ((this.tailLen > 0 ? this.tail[t++] : input[p++]) & 255) << 2;
                    this.tailLen -= t;
                    output[op++] = alphabet[v >> 12 & 63];
                    output[op++] = alphabet[v >> 6 & 63];
                    output[op++] = alphabet[v & 63];
                    if (this.do_padding) {
                        output[op++] = 61;
                    }

                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op++] = 13;
                        }

                        output[op++] = 10;
                    }
                } else if (this.do_newline && op > 0 && count != 19) {
                    if (this.do_cr) {
                        output[op++] = 13;
                    }

                    output[op++] = 10;
                }

                assert this.tailLen == 0;

                assert p == len;
            } else if (p == len - 1) {
                this.tail[this.tailLen++] = input[p];
            } else if (p == len - 2) {
                this.tail[this.tailLen++] = input[p];
                this.tail[this.tailLen++] = input[p + 1];
            }

            this.op = op;
            this.count = count;
            return true;
        }
    }

    static class Decoder extends Base64.Coder {
        private static final int[] DECODE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int SKIP = -1;
        private static final int EQUALS = -2;
        private final int[] alphabet;
        private int state;
        private int value;

        public Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return len * 3 / 4 + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            if (this.state == 6) {
                return false;
            } else {
                int p = offset;
                len += offset;
                int state = this.state;
                int value = this.value;
                int op = 0;
                byte[] output = this.output;
                int[] alphabet = this.alphabet;

                while(p < len) {
                    if (state == 0) {
                        while(p + 4 <= len && (value = alphabet[input[p] & 255] << 18 | alphabet[input[p + 1] & 255] << 12 | alphabet[input[p + 2] & 255] << 6 | alphabet[input[p + 3] & 255]) >= 0) {
                            output[op + 2] = (byte)value;
                            output[op + 1] = (byte)(value >> 8);
                            output[op] = (byte)(value >> 16);
                            op += 3;
                            p += 4;
                        }

                        if (p >= len) {
                            break;
                        }
                    }

                    int d = alphabet[input[p++] & 255];
                    switch(state) {
                        case 0:
                            if (d >= 0) {
                                value = d;
                                ++state;
                            } else if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                            break;
                        case 1:
                            if (d >= 0) {
                                value = value << 6 | d;
                                ++state;
                            } else if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                            break;
                        case 2:
                            if (d >= 0) {
                                value = value << 6 | d;
                                ++state;
                            } else if (d == -2) {
                                output[op++] = (byte)(value >> 4);
                                state = 4;
                            } else if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                            break;
                        case 3:
                            if (d >= 0) {
                                value = value << 6 | d;
                                output[op + 2] = (byte)value;
                                output[op + 1] = (byte)(value >> 8);
                                output[op] = (byte)(value >> 16);
                                op += 3;
                                state = 0;
                            } else if (d == -2) {
                                output[op + 1] = (byte)(value >> 2);
                                output[op] = (byte)(value >> 10);
                                op += 2;
                                state = 5;
                            } else if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                            break;
                        case 4:
                            if (d == -2) {
                                ++state;
                            } else if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                            break;
                        case 5:
                            if (d != -1) {
                                this.state = 6;
                                return false;
                            }
                    }
                }

                if (!finish) {
                    this.state = state;
                    this.value = value;
                    this.op = op;
                    return true;
                } else {
                    switch(state) {
                        case 0:
                        case 5:
                        default:
                            break;
                        case 1:
                            this.state = 6;
                            return false;
                        case 2:
                            output[op++] = (byte)(value >> 4);
                            break;
                        case 3:
                            output[op++] = (byte)(value >> 10);
                            output[op++] = (byte)(value >> 2);
                            break;
                        case 4:
                            this.state = 6;
                            return false;
                    }

                    this.state = state;
                    this.op = op;
                    return true;
                }
            }
        }
    }

    abstract static class Coder {
        public byte[] output;
        public int op;

        Coder() {
        }

        public abstract boolean process(byte[] var1, int var2, int var3, boolean var4);

        public abstract int maxOutputSize(int var1);
    }
}
