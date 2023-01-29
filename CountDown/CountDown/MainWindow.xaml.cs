using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace CountDown
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        bool stop_count_down = true;
        Thread count_down_thread;
        int minute = 10;
        int second = 0;

        public MainWindow()
        {
            InitializeComponent();
            this.Closing += MainWindow_Closing;
            RunCountDown();
        }

        private void MainWindow_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            count_down_thread.Abort();
        }

        public void RunCountDown()
        {
            count_down_thread = new Thread(() =>
            {
                try
                {
                    while (true)
                    {
                        Thread.Sleep(1000);
                        if (stop_count_down)
                            continue;

                        Dispatcher.Invoke(() =>
                        {
                            string s = count_down_text_block.Text;
                            string[] times = s.Split(':');
                            int minutes = Int32.Parse(times[0]);
                            int seconds = Int32.Parse(times[1]);

                            seconds--;
                            if (seconds == -1)
                            {
                                seconds = 59;
                                minutes--;
                            }

                            if (minutes == -1)
                            {
                                minutes = 0;
                                seconds = 0;
                                stop_count_down = true;
                                start_button.Content = "Start";

                                MessageBox.Show("\""+subject_text_block.Text + "\" time up!");
                            }


                            count_down_text_block.Text = minutes.ToString("D2") + ":" + seconds.ToString("D2");

                        });
                    }
                } catch (Exception)
                {

                }
            });

            count_down_thread.Start();
        }

        private void start_button_Click(object sender, RoutedEventArgs e)
        {
            if (stop_count_down)
                start_button.Content = "Stop";
            else
                start_button.Content = "Start";

            stop_count_down = !stop_count_down;
        }

        private void set_button_Click(object sender, RoutedEventArgs e)
        {
            SetTime w = new SetTime(minute, second);

            bool? b = w.ShowDialog();
            if (b.HasValue && b.Value)
            {
                stop_count_down = true;
                this.minute = w.minute;
                this.second = w.second;

                count_down_text_block.Text = String.Format("{0:D2}:{1:D2}", minute, second);
                start_button.Content = "Start";
            }
        }

        private void set_subject_button_Click(object sender, RoutedEventArgs e)
        {
            SetSubject w = new SetSubject(subject_text_block.Text);

            bool? b = w.ShowDialog();
            if (b.HasValue && b.Value)
            {
                subject_text_block.Text = w.subject;
            }
        }
    }
}
