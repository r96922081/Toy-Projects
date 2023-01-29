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
    /// Interaction logic for SetSubject.xaml
    /// </summary>
    public partial class SetSubject : Window
    {
        public string subject;
        public SetSubject(string subject)
        {
            InitializeComponent();
            this.subject = subject;
            subject_text_box.Text = subject;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (subject_text_box.Text.Trim() == "")
            {
                MessageBox.Show("Input non-empty subject");
                return;
            }

            subject = subject_text_box.Text;
            this.DialogResult = true;
            Close();
        }
    }
}
