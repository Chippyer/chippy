package com.chippy.log.compare.support;

import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.log.compare.annotation.MonitorField;
import com.chippy.log.compare.exception.CompareException;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @title: 通用操作日志比较核心处理器实现
 * 实现同类型均为{@link GenericCompareData}类型对象实例的比较
 * 并根据指定监控注解{@link MonitorField}监控字段变化值而生成日志实例
 * @author: chippy
 * @date: 2021-05-26 16:05
 **/
public abstract class GenericCompareProcessor<R extends GenericCompareData> implements CompareProcessor<R> {

    /**
     * 构建最终需要返回的日志类实例
     *
     * @param newCompareData new object(not null)
     * @param oldCompareData old(exists) object
     * @param expandField    monitor field
     * @return R
     * @author chippy
     * @date 2021-05-26 21:44
     */
    protected R buildOperateBo(GenericCompareData newCompareData, GenericCompareData oldCompareData,
        ExpandField expandField) {
        final String newFieldValue = String.valueOf(ReflectUtil.getFieldValue(newCompareData, expandField.getField()));
        if (ObjectsUtil.isEmpty(newFieldValue)) {
            return null;
        }
        // 通用字段信息构建
        final GenericCompareData genericCompareData = new GenericCompareData();
        genericCompareData.setNewItem(newFieldValue);
        genericCompareData.setOperationId(newCompareData.getOperationId());
        genericCompareData.setOperationName(newCompareData.getOperationName());
        if (Objects.nonNull(oldCompareData)) {
            genericCompareData
                .setOldItem(String.valueOf(ReflectUtil.getFieldValue(oldCompareData, expandField.getField())));
        }
        return (R)genericCompareData;
    }

    /**
     * 获取监控对象{@link Class}实例
     *
     * @return java.lang.Class<? extends com.gd.gcmp.pigeon.platform.service.operate.CompareOperate>
     * @author chippy
     * @date 2021-05-26 21:46
     */
    protected abstract Class<R> getClassInstance();

    /**
     * 传监控对象实例进行比较监控字段
     * 发生变化的监控字段赋值生成{@link GenericCompareData}实例返回
     * <p>
     * newCompareOperate(new object)参数不能为空！
     *
     * @param newCompareOperate new object(not null)
     * @param oldCompareOperate old(exists) object
     * @return java.util.List<com.gd.gcmp.pigeon.platform.service.operate.bo.OperateBo>
     * @author chippy
     * @date 2021-05-25 20:52
     */
    @Override
    public List<R> compareAndGet(GenericCompareData newCompareOperate, GenericCompareData oldCompareOperate) {
        if (Objects.isNull(newCompareOperate)) {
            throw new CompareException("对比新对象数据不能为空！");
        }
        final Class classType = this.getClassInstance();
        final List<ExpandField> monitorFieldList = this.getMonitorFieldList();
        // 特殊处理
        if (Objects.isNull(oldCompareOperate)) {
            if (!classType.equals(newCompareOperate.getClass())) {
                throw new CompareException("新对象对比监控对象不是同类型！");
            }
            return monitorFieldList.stream()
                .map(expandField -> this.buildOperateBo(newCompareOperate, null, expandField)).filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        // 通常处理
        if (!classType.equals(newCompareOperate.getClass()) || !classType.equals(oldCompareOperate.getClass())) {
            throw new CompareException("新对象对比老对象不是同类型！");
        }
        return monitorFieldList.stream().map(expandField -> {
            final Object newFieldValue = ReflectUtil.getFieldValue(newCompareOperate, expandField.getField());
            final Object oldFieldValue = ReflectUtil.getFieldValue(oldCompareOperate, expandField.getField());
            final boolean monitorFieldValueEq = Objects.equals(newFieldValue, oldFieldValue);
            return monitorFieldValueEq ? null : buildOperateBo(newCompareOperate, oldCompareOperate, expandField);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取监控字段集合
     *
     * @return java.util.List<java.lang.reflect.Field>
     * @author chippy
     * @date 2021-05-26 22:11
     */
    private List<ExpandField> getMonitorFieldList() {
        List<ExpandField> monitorFieldList = new LinkedList<>();
        final Field[] fields = ReflectUtil.getFields(this.getClassInstance());
        for (Field field : fields) {
            final MonitorField monitorField = field.getAnnotation(MonitorField.class);
            if (Objects.nonNull(monitorField)) {
                monitorFieldList.add(new ExpandField(field, monitorField.operateDesc()));
            }
        }
        return monitorFieldList;
    }

}