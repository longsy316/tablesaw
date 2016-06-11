package com.github.lwhite1.outlier.filter.dates;


import com.github.lwhite1.outlier.Table;
import com.github.lwhite1.outlier.filter.ColumnFilter;
import com.github.lwhite1.outlier.columns.ColumnReference;
import com.github.lwhite1.outlier.columns.LocalDateColumn;
import org.roaringbitmap.RoaringBitmap;

import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class LocalDateIsAfter extends ColumnFilter {

  int value;

  public LocalDateIsAfter(ColumnReference reference, int value) {
    super(reference);
    this.value = value;
  }

  @Override
  public RoaringBitmap apply(Table relation) {

    LocalDateColumn dateColumn = (LocalDateColumn) relation.column(columnReference().getColumnName());
    return dateColumn.isAfter(value);
  }
}