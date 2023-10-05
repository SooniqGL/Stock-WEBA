// Source File Name:   IntHashtable.java

package com.greenfield.ui.graph.util;

import java.util.Dictionary;
import java.util.Enumeration;

// Referenced classes of package JavaTest.util:
//            IntHashtableEntry, IntHashtableEnumerator

public class IntHashtable extends Dictionary
    implements Cloneable
{

    public IntHashtable(int i, float f)
    {
        if(i <= 0 || (double)f <= 0.0D)
        {
            throw new IllegalArgumentException();
        } else
        {
            _$5993 = f;
            _$5979 = new IntHashtableEntry[i];
            _$5984 = (int)((float)i * f);
            return;
        }
    }

    public IntHashtable(int i)
    {
        this(i, 0.75F);
    }

    public IntHashtable()
    {
        this(101, 0.75F);
    }

    public int size()
    {
        return _$5333;
    }

    public boolean isEmpty()
    {
        return _$5333 == 0;
    }

    public synchronized Enumeration keys()
    {
        return new IntHashtableEnumerator(_$5979, true);
    }

    public synchronized Enumeration elements()
    {
        return new IntHashtableEnumerator(_$5979, false);
    }

    public synchronized boolean contains(Object obj)
    {
        if(obj == null)
            throw new NullPointerException();
        IntHashtableEntry ainthashtableentry[] = _$5979;
        for(int i = ainthashtableentry.length; i-- > 0;)
        {
            IntHashtableEntry inthashtableentry = ainthashtableentry[i];
            while(inthashtableentry != null) 
            {
                if(inthashtableentry.value.equals(obj))
                    return true;
                inthashtableentry = inthashtableentry.next;
            }
        }

        return false;
    }

    public synchronized boolean containsKey(int i)
    {
        IntHashtableEntry ainthashtableentry[] = _$5979;
        int j = i;
        int k = (j & 0x7fffffff) % ainthashtableentry.length;
        for(IntHashtableEntry inthashtableentry = ainthashtableentry[k]; inthashtableentry != null; inthashtableentry = inthashtableentry.next)
            if(inthashtableentry.hash == j && inthashtableentry.key == i)
                return true;

        return false;
    }

    public synchronized Object get(int i)
    {
        IntHashtableEntry ainthashtableentry[] = _$5979;
        int j = i;
        int k = (j & 0x7fffffff) % ainthashtableentry.length;
        for(IntHashtableEntry inthashtableentry = ainthashtableentry[k]; inthashtableentry != null; inthashtableentry = inthashtableentry.next)
            if(inthashtableentry.hash == j && inthashtableentry.key == i)
                return inthashtableentry.value;

        return null;
    }

    public Object get(Object obj)
    {
        if(!(obj instanceof Integer))
        {
            throw new InternalError("key is not an Integer");
        } else
        {
            Integer integer = (Integer)obj;
            int i = integer.intValue();
            return get(i);
        }
    }

    protected void rehash()
    {
        int i = _$5979.length;
        IntHashtableEntry ainthashtableentry[] = _$5979;
        int j = i * 2 + 1;
        IntHashtableEntry ainthashtableentry1[] = new IntHashtableEntry[j];
        _$5984 = (int)((float)j * _$5993);
        _$5979 = ainthashtableentry1;
        for(int k = i; k-- > 0;)
        {
            IntHashtableEntry inthashtableentry = ainthashtableentry[k];
            while(inthashtableentry != null) 
            {
                IntHashtableEntry inthashtableentry1 = inthashtableentry;
                inthashtableentry = inthashtableentry.next;
                int l = (inthashtableentry1.hash & 0x7fffffff) % j;
                inthashtableentry1.next = ainthashtableentry1[l];
                ainthashtableentry1[l] = inthashtableentry1;
            }
        }

    }

    public synchronized Object put(int i, Object obj)
    {
        if(obj == null)
            throw new NullPointerException();
        IntHashtableEntry ainthashtableentry[] = _$5979;
        int j = i;
        int k = (j & 0x7fffffff) % ainthashtableentry.length;
        for(IntHashtableEntry inthashtableentry = ainthashtableentry[k]; inthashtableentry != null; inthashtableentry = inthashtableentry.next)
            if(inthashtableentry.hash == j && inthashtableentry.key == i)
            {
                Object obj1 = inthashtableentry.value;
                inthashtableentry.value = obj;
                return obj1;
            }

        if(_$5333 >= _$5984)
        {
            rehash();
            return put(i, obj);
        } else
        {
            IntHashtableEntry inthashtableentry1 = new IntHashtableEntry();
            inthashtableentry1.hash = j;
            inthashtableentry1.key = i;
            inthashtableentry1.value = obj;
            inthashtableentry1.next = ainthashtableentry[k];
            ainthashtableentry[k] = inthashtableentry1;
            _$5333++;
            return null;
        }
    }

    public Object put(Object obj, Object obj1)
    {
        if(!(obj instanceof Integer))
        {
            throw new InternalError("key is not an Integer");
        } else
        {
            Integer integer = (Integer)obj;
            int i = integer.intValue();
            return put(i, obj1);
        }
    }

    public synchronized Object remove(int i)
    {
        IntHashtableEntry ainthashtableentry[] = _$5979;
        int j = i;
        int k = (j & 0x7fffffff) % ainthashtableentry.length;
        IntHashtableEntry inthashtableentry = ainthashtableentry[k];
        IntHashtableEntry inthashtableentry1 = null;
        for(; inthashtableentry != null; inthashtableentry = inthashtableentry.next)
        {
            if(inthashtableentry.hash == j && inthashtableentry.key == i)
            {
                if(inthashtableentry1 != null)
                    inthashtableentry1.next = inthashtableentry.next;
                else
                    ainthashtableentry[k] = inthashtableentry.next;
                _$5333--;
                return inthashtableentry.value;
            }
            inthashtableentry1 = inthashtableentry;
        }

        return null;
    }

    public Object remove(Object obj)
    {
        if(!(obj instanceof Integer))
        {
            throw new InternalError("key is not an Integer");
        } else
        {
            Integer integer = (Integer)obj;
            int i = integer.intValue();
            return remove(i);
        }
    }

    public synchronized void clear()
    {
        IntHashtableEntry ainthashtableentry[] = _$5979;
        for(int i = ainthashtableentry.length; --i >= 0;)
            ainthashtableentry[i] = null;

        _$5333 = 0;
    }

    public synchronized Object clone()
    {
        try
        {
            IntHashtable inthashtable = (IntHashtable)super.clone();
            inthashtable._$5979 = new IntHashtableEntry[_$5979.length];
            for(int i = _$5979.length; i-- > 0;)
                inthashtable._$5979[i] = _$5979[i] == null ? null : (IntHashtableEntry)_$5979[i].clone();

            IntHashtable inthashtable1 = inthashtable;
            return inthashtable1;
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new InternalError();
        }
    }

    public synchronized String toString()
    {
        int i = size() - 1;
        StringBuffer stringbuffer = new StringBuffer();
        Enumeration enumeration = keys();
        Enumeration enumeration1 = elements();
        stringbuffer.append("{");
        for(int j = 0; j <= i; j++)
        {
            String s = enumeration.nextElement().toString();
            String s1 = enumeration1.nextElement().toString();
            stringbuffer.append(s + "=" + s1);
            if(j < i)
                stringbuffer.append(", ");
        }

        stringbuffer.append("}");
        return stringbuffer.toString();
    }

    private IntHashtableEntry _$5979[];
    private int _$5333;
    private int _$5984;
    private float _$5993;
}
