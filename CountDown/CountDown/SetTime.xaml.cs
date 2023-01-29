using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace CountDown
{
    /// <summary>
    /// Interaction logic for SetTime.xaml
    /// </summary>
    public partial class SetTime : Window
    {
        public int minute = 10;
        public int second = 0;

        public SetTime(int minute, int second)
        {
            this.minute = minute;
            this.second = second;

            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            int minute;
            int second;

            if (Int32.TryParse(minute_text_box.Text, out minute) == false || minute > 60 || minute < 0)
            {
                MessageBox.Show("Input minute between 0 - 60");
                return;
            }

            if (Int32.TryParse(second_text_box.Text, out second) == false || second > 60 || second < 0)
            {
                MessageBox.Show("Input second between 0 - 60");
                return;
            }


            this.minute = minute;
            this.second = second;
            this.DialogResult = true;
            Close();
        }
    }
}
