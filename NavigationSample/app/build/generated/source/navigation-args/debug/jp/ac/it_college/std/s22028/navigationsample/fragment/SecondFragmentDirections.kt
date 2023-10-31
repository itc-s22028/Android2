package jp.ac.it_college.std.s22028.navigationsample.fragment

import android.os.Bundle
import androidx.navigation.NavDirections
import jp.ac.it_college.std.s22028.navigationsample.R
import kotlin.Int

public class SecondFragmentDirections private constructor() {
  private data class ActionSecondFragmentToThirdFragment(
    public val choice: Int = 0,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_secondFragment_to_thirdFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("choice", this.choice)
        return result
      }
  }

  public companion object {
    public fun actionSecondFragmentToThirdFragment(choice: Int = 0): NavDirections =
        ActionSecondFragmentToThirdFragment(choice)
  }
}
