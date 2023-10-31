package jp.ac.it_college.std.s22028.navigationsample.fragment

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class ThirdFragmentArgs(
  public val choice: Int = 0,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("choice", this.choice)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("choice", this.choice)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ThirdFragmentArgs {
      bundle.setClassLoader(ThirdFragmentArgs::class.java.classLoader)
      val __choice : Int
      if (bundle.containsKey("choice")) {
        __choice = bundle.getInt("choice")
      } else {
        __choice = 0
      }
      return ThirdFragmentArgs(__choice)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ThirdFragmentArgs {
      val __choice : Int?
      if (savedStateHandle.contains("choice")) {
        __choice = savedStateHandle["choice"]
        if (__choice == null) {
          throw IllegalArgumentException("Argument \"choice\" of type integer does not support null values")
        }
      } else {
        __choice = 0
      }
      return ThirdFragmentArgs(__choice)
    }
  }
}
