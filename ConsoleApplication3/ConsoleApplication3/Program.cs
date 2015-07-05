using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Net;
using System.Web;
using System.Windows.Forms;
using System.Data;
using System.Runtime.InteropServices;
using System.Diagnostics;
using System.Threading;
using System.Drawing;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using FireSharp;
using FireSharp.Serialization.ServiceStack;
using FireSharp.Serialization.JsonNet;
namespace ConsoleApplication3
{
    class PR
    {
        static void Main(string[] args)
        {
            long previous = 0;
            while (true)
            {
                string wholestring = getStatus();
                string method = "";
                string action = "";
                string keytype = "";
                string locationX = "";
                string locationY = "";
                string phoneX = "";
                string phoneY = "";
                string oldString = wholestring;
                method = wholestring.Substring(0, 1);

                string[] useage = wholestring.Split(new Char[] { '|' });
                long time = Convert.ToInt32(useage[useage.Length - 1]);
                if ((previous >= time) | (previous == 0))
                {
                    previous = time;
                    continue;
                }
                previous = time;

                //Mouse Movement
                if (method.Equals("A"))
                {
                    locationX = useage[2];
                    locationY = useage[3];
                    phoneX = useage[4];
                    phoneY = useage[5];
                    int deskHeight = Screen.PrimaryScreen.Bounds.Height;
                    int deskWidth = Screen.PrimaryScreen.Bounds.Width;
                    double ratioX = 0.0;
                    double ratioY = 0.0;
                    float phonesizeX = float.Parse(phoneX);
                    float phonesizeY = float.Parse(phoneY);
                    float mouselocationX = float.Parse(locationX);
                    float mouselocationY = float.Parse(locationY);
                    ratioX = deskWidth / phonesizeX;
                    ratioY = deskHeight / phonesizeY;
                    int finallocationX = (int)(mouselocationX * ratioX);
                    int finallocationY = (int)(mouselocationY * ratioY);
                    MouseMove(finallocationX, finallocationY,1);

                    int oldlocationX = finallocationX;
                    int oldlocationY = finallocationY;
                    //MessageBox.Show("Your screen resolution is " + deskWidth + "x" + deskHeight);
                }

                

                // Keyboard typing 
                if (method.Equals("B"))
                {

                    Thread.Sleep(1);
                    keytype = useage[0].Substring(1,1);
                    switch (keytype)
                    {
                        case "(":
                            SendKeys.SendWait("{(}");
                            break;
                        case ")":
                            SendKeys.SendWait("{)}");
                            break;
                        case "+":
                            SendKeys.SendWait("{+}");
                            break;
                        case "^":
                            SendKeys.SendWait("{^}");
                            break;
                        default:
                            SendKeys.SendWait(keytype.ToLower());
                            break;
                    }
                }
            




                //Basic Action
                if (method.Equals("C"))
                {
                    //Thread.Sleep(150);
                    action = useage[0].Substring(1, 1);
                    switch (action)
                    {
                        case "0":
                            Console.WriteLine("Case lock");
                            Process.Start(@"C:\WINDOWS\system32\rundll32.exe", "user32.dll,LockWorkStation");
                            break;
                        case "1":
                            Console.WriteLine("Case Close current program");
                            SendKeys.SendWait("%{F4}");
                            Console.WriteLine("Closing is working");
                            break;
                        case "2":
                            Console.WriteLine("Case Right key");
                            SendKeys.SendWait("{RIGHT}");
                            break;
                        case "3":
                            Console.WriteLine("Case Left key");
                            SendKeys.SendWait("{LEFT}");
                            break;
                        case "4":
                            Console.WriteLine("Case Up");
                            SendKeys.SendWait("{UP}");
                            break;
                        case "5":
                            Console.WriteLine("Case Down");

                            SendKeys.SendWait("{DOWN}");
                            break;
                        case "6":
                            Console.WriteLine("Case Enter");
                            SendKeys.SendWait("{ENTER}");
                            break;
                        default:
                            Console.WriteLine("Default case");
                            break;
                    }
                }
            }
        }
           


        public static string  getStatus()
        {
            var client = new WebClient();
            string html = client.DownloadString("https://mousemove.firebaseio.com/.json");
            if (html.Length != 0)
            {
                html = html.Substring(1, html.Length - 2);
                return html;
            }
            return "A|0";
        }

        [DllImport("user32.dll")]
        static extern void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);
        public static void MouseMove(int x, int y,double durationSecs)
        {
            Cursor.Position = new System.Drawing.Point(x, y);
        }
    }
}
