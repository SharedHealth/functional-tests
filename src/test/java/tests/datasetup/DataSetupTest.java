package tests.datasetup;

import categories.DatasetupTest;
import domain.DataSetup;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
@Category(DatasetupTest.class)
public class DataSetupTest {
  @Test
  public void setupData() throws IOException {
    new DataSetup().setup();
  }
}
