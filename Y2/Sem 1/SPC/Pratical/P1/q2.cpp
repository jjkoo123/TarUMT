#include <iostream>
using namespace std;

int main()
{
    int employee[7][7] = {{2, 4, 3, 4, 5, 8, 8}, {7, 3, 4, 3, 3, 4, 4}, {3, 4, 4, 6, 3, 4, 4}, {7, 3, 4, 8, 3, 4, 4}, {3, 5, 4, 3, 6, 3, 8}, {3, 4, 4, 6, 3, 4, 4}, {6, 3, 5, 9, 2, 7, 9}};

    int i, j, totalHours[7] = {0};

    cout << "\t\t" << "Sun Mon Tue Wed Thu Fri Sat" << endl;

    for (i = 0; i < 7; i++)
    {
        cout << "Employee " << i << "\t";

        for (j = 0; j < 7; j++)
        {
            totalHours[i] += employee[i][j];
            cout << employee[i][j] << "   ";
        }
        cout << totalHours[i] << endl;
    }
    return 0;
}