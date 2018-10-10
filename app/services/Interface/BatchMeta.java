package services.Interface;

import com.google.inject.ImplementedBy;
import models.Batch;
import services.Implementation.BatchImpl;

@ImplementedBy(BatchImpl.class)
public interface BatchMeta extends Generic<Batch> {

}
