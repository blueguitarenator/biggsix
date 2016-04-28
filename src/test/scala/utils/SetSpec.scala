package utils

import org.scalatest.FlatSpec

/**
  * Created by rich.johnson on 4/27/16.
  */
class SetSpec extends FlatSpec {
  "An empty Set" should "have size 0" in {
    assert(Set.empty.size == 0)
  }

  it should "produce NoSuchElementException when head is invoked" in {
    intercept[NoSuchElementException] {
      Set.empty.head
    }
  }
}
