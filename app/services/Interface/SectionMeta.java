package services.Interface;

import com.google.inject.ImplementedBy;
import models.Section;
import services.Implementation.SectionImpl;

@ImplementedBy(SectionImpl.class)
public interface SectionMeta extends Generic<Section> {

}
