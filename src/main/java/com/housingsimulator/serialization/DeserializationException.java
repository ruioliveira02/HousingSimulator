package com.housingsimulator.serialization;

/**
 * An error in the deserialization process
 * Defines useful methods to get the error's position and cause
 */
public class DeserializationException extends Exception
{
    private final CharSequence input; /*! The input that caused the error */
    private final int charPosition;   /*! The position in the input where the error occurred */

    /**
     * Returns the position where the error occurred
     *
     * @return The position where the error occurred
     */
    public final int getPosition() {
        return this.charPosition;
    }

    /**
     * Creates a DeserializationException
     *
     * @param msg       The error message to display
     * @param position  The position in the input where the error occurred
     * @param input     The input that caused the error
     */
    public DeserializationException(String msg, int position, CharSequence input) {
        this(msg, position, input, null);
    }

    /**
     * Creates a DeserializationException with the specified cause
     *
     * @param msg       The error message to display
     * @param position  The position in the input where the error occurred
     * @param input     The input that caused the error
     * @param cause     The cause of the error
     */
    public DeserializationException(String msg, int position, CharSequence input, Throwable cause)
    {
        super(msg, cause);
        this.input = input;
        this.charPosition = position;
    }

    /**
     * Creates a DeserializationException with the specified nested DeserializationException cause
     *
     * @param msg           The error message to display
     * @param causePosition The beginning of the cause's input inside this object's input
     * @param cause         The cause of the error
     * @param input         The input that caused the error
     */
    public DeserializationException(String msg, int causePosition, DeserializationException cause, CharSequence input)
    {
        super(msg, cause);
        this.charPosition = causePosition + cause.charPosition;
        this.input = input;
    }

    /**
     * Returns a string containing the part of the input that caused the error, pointed at by a '^' character
     *
     * @return  The string
     */
    private String getPreview()
    {
        //TODO: cut input if line breaks are found
        int begin = Math.max(charPosition - 20, 0);
        int end = Math.min(charPosition + 50, input.length());

        String prefix = (begin == 0 ? "" : "...");
        String suffix = (end == input.length() ? "" : "...");
        String aux = prefix + input.subSequence(begin, end) + suffix;

        StringBuilder sb = new StringBuilder();
        aux.chars().limit(prefix.length() + charPosition - begin)
                .map(i -> (char)i == '\t' ? i : ( (char)i == '\n' ? (int)'\r' : (int)' '))
                .forEachOrdered(sb::appendCodePoint);

        return aux + "\n" + sb.toString() + "^";
    }

    /**
     * Returns this object's representation as a string, if possible showing a preview of the error's position
     *
     * @return  This object's representation as a string
     */
    @Override
    public String toString()
    {
        StringBuilder ans = new StringBuilder();
        ans.append(super.toString());

        if (this.input != null)
        {
            int line = 1, column = 1;

            for (int i = 0; i < charPosition; i++, column++) {
                if (input.charAt(i) == '\n') {
                    line++;
                    column = 0;
                }
            }

            ans.append(String.format("\n\tin line %d, column %d\n", line, column));
            ans.append(this.getPreview());
        }
        else if (getCause() instanceof DeserializationException)
            ans.append(((DeserializationException)getCause()).getPreview());

        return ans.toString();
    }
}
