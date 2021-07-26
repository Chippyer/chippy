package com.chippy.log.compare.support;

import cn.hutool.core.util.ReflectUtil;
import com.chippy.common.utils.ObjectsUtil;
import com.chippy.log.compare.annotation.MonitorField;
import com.chippy.log.compare.exception.CompareException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @title: 通用操作日志比较核心处理器实现
 * 实现同类型均为{@link CompareData}类型对象实例的比较
 * 并根据指定监控注解{@link MonitorField}监控字段变化值而生成日志实例
 * @author: chippy
 * @date: 2021-05-26 16:05
 **/
public class GenericCompareProcessor<C extends CompareData> implements CompareProcessor<C, GenericCompareData> {

    private Map<String, List<ExpandField>> monitorExpandFieldMap = new HashMap<>();
    private Map<String, Class<?>> monitorClassMap = new HashMap<>();

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
    public List<GenericCompareData> compareAndGet(CompareData newCompareOperate, CompareData oldCompareOperate) {
        if (Objects.isNull(newCompareOperate)) {
            throw new CompareException("对比新对象数据不能为空！");
        }
        this.doInitMonitorRelationInfoIfNecessary(newCompareOperate);
        final String className = newCompareOperate.getClass().getName();
        final List<ExpandField> monitorFieldList = this.getMonitorFieldList(className);
        // old object is empty
        if (Objects.isNull(oldCompareOperate)) {
            return monitorFieldList.stream()
                .map(expandField -> this.buildOperateBo(newCompareOperate, null, expandField)).filter(Objects::nonNull)
                .collect(Collectors.toList());
        }

        if (!newCompareOperate.getClass().equals(oldCompareOperate.getClass())) {
            throw new CompareException("新对象对比老对象不是同类型！");
        }
        return monitorFieldList.stream().map(expandField -> {
            final Object newFieldValue = ReflectUtil.getFieldValue(newCompareOperate, expandField.getField());
            final Object oldFieldValue = ReflectUtil.getFieldValue(oldCompareOperate, expandField.getField());
            final boolean monitorFieldValueEq = Objects.equals(newFieldValue, oldFieldValue);
            return monitorFieldValueEq ? null : this.buildOperateBo(newCompareOperate, oldCompareOperate, expandField);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void doInitMonitorRelationInfoIfNecessary(CompareData newCompareOperate) {
        final Class<? extends CompareData> compareOperateClass = newCompareOperate.getClass();
        final String className = compareOperateClass.getName();
        if (ObjectsUtil.isNotEmpty(monitorClassMap.get(className))) {
            return;
        }
        this.monitorClassMap.put(className, compareOperateClass);
        this.monitorExpandFieldMap.put(className, this.getMonitorFieldList(newCompareOperate));
    }

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
    private GenericCompareData buildOperateBo(CompareData newCompareData, CompareData oldCompareData,
        ExpandField expandField) {
        final String newFieldValue = String.valueOf(ReflectUtil.getFieldValue(newCompareData, expandField.getField()));
        if (ObjectsUtil.isEmpty(newFieldValue)) {
            return null;
        }
        // 通用字段信息构建
        final GenericCompareData genericCompareData = new GenericCompareData();
        genericCompareData.setItemName(expandField.getField().getName());
        genericCompareData.setNewItem(newFieldValue);
        genericCompareData.setOperationId(newCompareData.getOperationId());
        genericCompareData.setOperationName(newCompareData.getOperationName());
        if (newCompareData instanceof OperationRelationCompareData) {
            final OperationRelationCompareData operationRelationCompareData =
                (OperationRelationCompareData)newCompareData;
            genericCompareData.setOperationType(operationRelationCompareData.getOperationType());
        }
        if (Objects.nonNull(oldCompareData)) {
            genericCompareData
                .setOldItem(String.valueOf(ReflectUtil.getFieldValue(oldCompareData, expandField.getField())));
        }
        return genericCompareData;
    }

    private List<ExpandField> getMonitorFieldList(String className) {
        return this.monitorExpandFieldMap.get(className);
    }

    private List<ExpandField> getMonitorFieldList(CompareData newCompareOperate) {
        final Field[] fields = ReflectUtil.getFields(newCompareOperate.getClass());
        if (ObjectsUtil.isEmpty(fields)) {
            return Collections.emptyList();
        }
        final List<ExpandField> expandFieldList = new ArrayList<>(fields.length);
        for (Field field : fields) {
            final MonitorField monitorField = field.getAnnotation(MonitorField.class);
            if (Objects.nonNull(monitorField)) {
                expandFieldList.add(new ExpandField(field, monitorField.operateDesc()));
            }
        }
        return expandFieldList;
    }

}