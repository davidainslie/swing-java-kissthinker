package com.kissthinker.swing.table;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.table.AbstractTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kissthinker.collection.map.BiMap;
import com.kissthinker.collection.map.MapListener;
import com.kissthinker.javabean.*;
import com.kissthinker.reflect.FieldUtil;
import com.kissthinker.text.StringUtil;
import static com.kissthinker.collection.array.ArrayUtil.va;
import static com.kissthinker.collection.map.MapUtil.biMap;

/**
 * TODO Caching e.g. no need to always calculate the "property fields".
 * <br/>
 * @author David Ainslie
 *
 * @param <K> A {@link JavaBean} key where this key a {@link Map} key and a  {@link JavaBean} implementation is a {@link Map} value
 * @param <B> Any class is a {@link JavaBean} implementation
 */
public class JavaBeanTableModel<K, B extends JavaBean> extends AbstractTableModel
                                                       implements MapListener<K, B>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaBeanTableModel.class);
    
    /** */
    private final BiMap<Integer, K> keysPerRowIndex = biMap();
    
    /** */
    private final BiMap<Integer, B> javaBeansPerRowIndex = biMap();
    
    /** */
    private final Map<K, B> javaBeansPerKey;
    
    /** */
    private final Class<B> javaBeanClass;
    
    /** */
    private final AtomicBoolean propertyFieldsSet = new AtomicBoolean(false);
    
    /** */
    private Field[] propertyFields;    

    /**
     * As no properties are provided, all fields marked up with {@link Property} will be displayed as columns in a table.
     */
    public JavaBeanTableModel(final Map<K, B> javaBeansPerKey, Class<B> javaBeanClass)
    {
        assert(javaBeansPerKey != null) : "Please provide a non null Map";
        this.javaBeansPerKey = javaBeansPerKey;
        this.javaBeanClass = javaBeanClass;
        
        JavaBeanSupport.listen(javaBeansPerKey, this);
        
        for (Entry<K, B> entry : javaBeansPerKey.entrySet())
        {
            int rowIndex = keysPerRowIndex.size();
            keysPerRowIndex.put(rowIndex, entry.getKey());
            javaBeansPerRowIndex.put(rowIndex, entry.getValue());
            
            JavaBeanSupport.listen(entry.getValue(), new PropertyListener<Object>()
            {
                /**
                 * 
                 * @see com.kissthinker.javabean.PropertyListener#propertyChange(com.kissthinker.javabean.PropertyEvent)
                 */
                @Override
                public void propertyChange(PropertyEvent<Object> propertyEvent)
                {
                    B javaBean = propertyEvent.source();
                    int rowIndex = javaBeansPerRowIndex.key(javaBean);
                    fireTableCellUpdated(rowIndex, 1); // TODO Remove hardcoded column of 1
                }                
            });
        }
    } 

    /**
     * 
     * @param javaBeansPerKey
     * @param properties a JavaBean's properties, exposed as enums, to be displayed as columns in a table.
     */
    @SafeVarargs
    public <P extends Enum<P>> JavaBeanTableModel(final Map<K, B> javaBeansPerKey, Class<B> javaBeanClass, P... properties)
    {
        this(javaBeansPerKey, javaBeanClass);
        // TODO
    }

    /**
     * 
     * @param javaBeansPerKey
     * @param properties the names of a JavaBean's properties to be displayed as columns in a table.
     */
    public JavaBeanTableModel(final Map<K, B> javaBeansPerKey, Class<B> javaBeanClass, String... properties)
    {
        this(javaBeansPerKey, javaBeanClass);
        // TODO        
    }

    /**
     * 
     * @see com.kissthinker.collection.map.MapListener#onPut(java.lang.Object, java.lang.Object)
     */
    @Override
    public void onPut(K key, B value)
    {
        int rowIndex = keysPerRowIndex.size();
        keysPerRowIndex.put(rowIndex, key);
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    /**
     * 
     * @see com.kissthinker.collection.map.MapListener#onRemove(java.lang.Object, java.lang.Object)
     */
    @Override
    public void onRemove(K key, B value)
    {
        javaBeansPerKey.remove(key);
        int rowIndex = keysPerRowIndex.removeKey(key);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * 
     * @param columnName
     * @param property
     * @return
     */
    public <P extends Enum<P>> JavaBeanTableModel<K, B> property(String columnName, P property)
    {
        // TODO
        return this;
    }

    /**
     * 
     * @param columnName
     * @param property
     * @return
     */
    public JavaBeanTableModel<K, B> property(String columnName, String property)
    {
        // TODO
        return this;
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        return javaBeansPerKey.size();
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount()
    {
        LOGGER.info("{}", Thread.currentThread());
        
        if (propertyFields() == null)
        {
            LOGGER.info("propertyFields = null, {}", Thread.currentThread());
            return 0;
            //return 4;
        }
        
        return propertyFields.length;
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        B javaBean = javaBeansPerKey.get(keysPerRowIndex.value(rowIndex));
        
        if (javaBean == null || propertyFields() == null)
        {
            return null;
        }
        
        // Unless all field properties have an "index", we can only map the "columnIndex" to that index in the "property fields" array.
        for (Field propertyField : propertyFields())
        {
            Property property = propertyField.getAnnotation(Property.class);
            
            if (property.index() == -1)
            {
                return FieldUtil.value(propertyFields()[columnIndex], javaBean);
            }
        }
        
        // Get value from "property field" that has an "index" matching given "columnIndex".
        for (Field propertyField : propertyFields())
        {
            Property property = propertyField.getAnnotation(Property.class);
            
            if (property.index() == columnIndex)
            {
                return FieldUtil.value(propertyField, javaBean);
            }
        }
        
        LOGGER.warn("Should not have reached this point. Given rowIndex and columnIndex are ({}, {}) for javaBean {}", va(rowIndex, columnIndex, javaBean));
        return null;
    }

    /**
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        Object value = getValueAt(0, columnIndex);
        
        // Note that super call actually has return of Object.class hardcoded.
        return value == null ? super.getColumnClass(columnIndex) : value.getClass();
    }

    /**
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex)
    {
        if (propertyFields() == null)
        {
            return null;
        }
                
        // Unless all field properties have an "index", we can only map the "columnIndex" to that index in the "property fields" array.
        for (Field propertyField : propertyFields())
        {
            Property property = propertyField.getAnnotation(Property.class);
            
            if (property.index() == -1)
            {
                property = propertyFields()[columnIndex].getAnnotation(Property.class);
                        
                if ("".equals(property.name()))
                {
                    return StringUtil.title(propertyFields()[columnIndex].getName());
                }
                else
                {
                    StringUtil.title(property.name());
                }
            }
        }
        
        // Get value from "property field" that has an "index" matching given "columnIndex".
        for (Field propertyField : propertyFields())
        {
            Property property = propertyField.getAnnotation(Property.class);
            
            if (property.index() == columnIndex)
            {
                if ("".equals(property.name()))
                {
                    return StringUtil.title(propertyFields()[columnIndex].getName());
                }
                else
                {
                    StringUtil.title(property.name());
                }
            }
        }
        
        LOGGER.warn("Should not have reached this point. Given columnIndex is {}", columnIndex);
        return null;
    }
    
    /**
     * 
     * @return
     */
    private Field[] propertyFields()
    {
        if (propertyFields == null)
        {
            // For thread safety, only allow one thread into the following block if "propertyFieldsSet" has itself, not be set.
            if (propertyFieldsSet.compareAndSet(false, true))
            {
                // TODO CHECK FOR GIVEN PROPERTIES AS STRINGS OR ENUMS IN CONSTRUCTOR
                
                LOGGER.info("Thread {} initialising property fields", Thread.currentThread());
                
                propertyFields = FieldUtil.fields(javaBeanClass, Property.class);
                
                if (propertyFields == null)
                {
                    LOGGER.warn("No property fields (no fields marked up with @Property) for {}", javaBeanClass);
                }
                else
                {
                    LOGGER.info("Property fields initialised within thread {}", Thread.currentThread());
                }
            }
            
            if (propertyFields == null)
            {
                LOGGER.info("Property fields is null and some other thread is initialising, so this thread, {}, will try again to acquire", Thread.currentThread());
                
                try
                {
                    TimeUnit.MILLISECONDS.sleep(20);
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                return propertyFields();
            }            
        }
        
        return propertyFields;
    }
}