
package com.marcospassos.phpserializer.state;

import com.marcospassos.phpserializer.WriterState;

/**
 * Represents the state of the writer while writing a value.
 *
 * @author Marcos Passos
 * @since 1.0
 */
public class WritingValueState extends AbstractState
{
    /**
     * The parent state.
     */
    WriterState parent;

    /**
     * Creates an initial state.
     */
    public WritingValueState()
    {
        this(new FinishedState());
    }

    /**
     * Creates a new state from the specified parent state.
     *
     * @param parent The parent state.
     */
    public WritingValueState(WriterState parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean isReferable()
    {
        return true;
    }

    @Override
    public WriterState serializableBegin()
    {
        return new WritingSerializableObjectState(parent);
    }

    @Override
    public WriterState objectBegin()
    {
        return new WritingObjectState(parent);
    }

    @Override
    public WriterState value()
    {
        return parent;
    }

    @Override
    public WriterState arrayBegin()
    {
        return new WritingArrayState(parent);
    }
}
