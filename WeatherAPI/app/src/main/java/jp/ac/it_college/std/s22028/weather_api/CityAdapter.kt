import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import jp.ac.it_college.std.s22028.weather_api.City
import jp.ac.it_college.std.s22028.weather_api.databinding.RowBinding

class CityAdapter(
    context: Context,
    private val callback: (City) -> Unit,
    private val cities: List<City>
) : ArrayAdapter<City>(context, 0, cities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: RowBinding =
            if (convertView == null) {
                RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            } else {
                RowBinding.bind(convertView)
            }

        val city = getItem(position)
        binding.name.text = city?.name
        binding.name.setOnClickListener {
            city?.let { callback(it) }
        }

        return binding.root
    }
}
