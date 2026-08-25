from tkinter import *
import tkinter as tk
from tkinter import ttk
from tkinter import messagebox, filedialog
import pygame
from tkcalendar import DateEntry
import time
import json
import threading 
from threading import Timer
import datetime
import os
import random

#Initialize pygame mixer for sound
pygame.mixer.init()

class Quizz():
    def __init__ (self,master):
        self.quiz = master
        #json fike to store my data
        self.file_quiz = "Quizz.json"

        #progress and card left is to track the progress
        self.progresss = 0
        self.cardleft = 0

        #list to hold flashcards
        self.question = []

        #list to hold falshcards for quiz
        self.question_quiz =[]

        #list to hold the flashcard that being created
        self.deck = []

        #to detect correct and wrong amount
        self.quiz_correct = 0
        self.quiz_wrong = 0

        #load my json file
        self.load_quiz()

        #set column and row
        self.quiz.columnconfigure((0,1,2,4),weight = 1)
        self.quiz.rowconfigure((0,1,2,3,4,5),weight = 1)

    # #Its a frame to adjust the layout
        self.flashcard_label = Label(self.quiz,text = 'Flashcard Quizzer',font = ('Helvetica',30,'bold'),width = 60,bg = '#4CAF50',fg = 'white')
        self.flashcard_label.grid(row = 0,column=0,columnspan=4,sticky='news',pady=10)
        
        #Its a flash card button
        self.flashCard = Button(self.quiz,text = f"FLASHCARD REMAINS :  {len(self.question)}",font = ('Helvetica',30,'bold'),command = self.check_deck_flashcard,pady= 10,bd=5,highlightthickness=0,bg = '#4CAF50',fg = 'white')
        self.flashCard.grid(row = 1,column=0,columnspan=4,rowspan=2,sticky='news',padx = 20,pady = 20)
        
        #Its a add card button
        self.addCard = Button(self.quiz,text = " + ADD NEW CARD", cursor = 'plus', font = ('Helvetica',30,'bold'),pady=10, width = 20, bd=5,highlightthickness=0,command=self.addcard_section,bg = '#4CAF50',fg = 'white')
        self.addCard.grid(row = 3,column=0,sticky = 'news',padx = 20)

        #label for flash card revised
        self.progress = Label(self.quiz,text = f"FLASH CARD \n  REVISED :  \n{self.cardleft} / {self.progresss}",font = ('Helvetica',25,'bold'),pady=10,width = 20,bd=5,highlightthickness=0,  bg = '#4CAF50',fg = 'white',wraplength=200)
        self.progress.grid(row = 3,column=1,columnspan=3,sticky='news',padx = 20)
        
        #button for quiz mode
        self.quizmode = Button(self.quiz,text = " QUIZ MODE ",cursor = 'circle',font = ('Helvetica',30,'bold'),pady=10,width = 20,bd=5, command=self.check_deck_quiz, bg = '#4CAF50',fg = 'white' )
        self.quizmode.grid(row = 4,rowspan=2,column=0,columnspan=3,sticky='news',padx = 20,pady = 20)

        #button for deck check
        self.deck_check = Button(self.quiz,text = f" TOTAL CARD :\n {len(self.deck)}",cursor = 'circle',font = ('Helvetica',30,'bold'),pady=10,width = 20, bd=5,highlightthickness=0,  command=self.check_decks,bg = '#4CAF50',fg = 'white')
        self.deck_check.grid(row = 4,rowspan=2,column=3,sticky='news',padx = 20,pady = 20)

    #To check the total deck 
    def check_decks(self):
        if len(self.deck) == 0:
            messagebox.showinfo("Attention","DECK IS EMPTY,PLEASE ADD NEW CARD ;)") 
        else:
            self.total_deck()

####Its a place to check total card being added
    def total_deck(self):
        self.deck_top = Toplevel()
        self.deck_top.title('FLASHCARD')

        self.deck_top.configure(bg= '#4CAF50')

        self.deck_top.columnconfigure((0,1),weight = 1)
        self.deck_top.rowconfigure((0,1,2,3,4,5,6),weight = 1)

        # Get screen width and height
        screen_width = self.deck_top.winfo_screenwidth()
        screen_height = self.deck_top.winfo_screenheight()

        #Set top level suze
        create_fc_width = 1000
        create_fc_height = 700

        #Calculation for the position of the top level window
        x_position = (screen_width // 2) - (create_fc_width // 2)
        y_position = (screen_height // 2) - (create_fc_height // 2)

        # Set the geometry of the Toplevel window
        self.deck_top.geometry(f"{create_fc_width}x{create_fc_height}+{x_position}+{y_position}")
            
        #To ensure the window wont be smaller than the fixed size
        self.deck_top.minsize(1000,700)

        #title section
        self.deck_label = Label(self.deck_top,text = "TOTAL CARD",font = ('Helvetica',40,'bold'),bg = '#4CAF50',fg = 'white')
        self.deck_label.grid(row = 0,column = 0,columnspan = 2,sticky = 'news')

        #content of the cards
        self.deck_content = Listbox(self.deck_top,bg = 'white',font = ('Helvetica',40,'bold'))
        self.deck_content.grid(row = 1,rowspan=5,column=0,columnspan=2,sticky='news',padx = 30,pady=30)

        #to insert value into listbox
        for i in self.deck:
            message = f"QUESTION : {i['question_deck']} ANSWER : {i['answer_deck']}"
            self.deck_content.insert(tk.END,message)

        self.deck_content_function = Button(self.deck_top,text = "BACK",command=self.return_deck,font = ('Helvetica',40,'bold'),bg= '#4CAF50',fg='white',highlightthickness=0,bd =0)
        self.deck_content_function.grid(row = 6,column = 0,sticky = 'news',pady =30)

        self.deck_content_function = Button(self.deck_top,text = "DELETE",command=self.delete_deck,font = ('Helvetica',40,'bold'),bg= '#4CAF50',fg='white',highlightthickness=0,bd = 0)
        self.deck_content_function.grid(row = 6,column = 1,sticky = 'news',pady =30)

    def return_deck(self):
        if messagebox.askokcancel("Attention","ARE YOU SURE YOU'RE DONE VIEWWING CARDS ? "):
            self.deck_top.destroy()
        else:
            pass

    def delete_deck(self):
        # Get the index of the selected item in the Listbox
        selected_index = self.deck_content.curselection()
        
        if not selected_index:
            messagebox.showwarning("Attention","YOU DIDN'T CHOOSE ANYTHING TO DELETE")
            return
        
        index = selected_index[0]

        # Ask for user confirmation
        if messagebox.askokcancel("Attention", "ARE YOU SURE YOU WANT TO DELETE THE SELECTED CARD?"):
            self.deck_content.delete(index)
                    
            
            # Delete the corresponding items from the lists
            del self.question[index]
            del self.question_quiz[index]
            del self.deck[index]

            self.cardleft -=1 #update the amount of cardleft

            # Update GUI    
            self.flashCard.config(text=f"FLASHCARD REMAINS: {len(self.question)}")
            self.progress.config(text=f"FLASH CARD\nREVISED:\n{self.cardleft} / {self.progresss}")
            self.deck_check.config(text = f" TOTAL CARD :\n {len(self.deck)}")
                    
            #successful delete item info
            messagebox.showinfo("Attention", "YOU'VE DELETED THE SELECTED ITEM SUCCESSFULLY")
            self.save_quiz()
        else:
            # User canceled deletion
            pass

    #To check wether the deck still got card or not for flashcard
    def check_deck_flashcard(self):
        if len(self.question) == 0:
            messagebox.showinfo("Attention","FLASHCARD NEED TO BE REVISED IS EMPTY,PLEASE ADD NEW CARD ;)")
        else:
            self.flashcard()

    #To check wether the deck still got card or not for quiz mode
    def check_deck_quiz(self):
        if self.cardleft == 0:
            messagebox.showinfo("Attention","You've answered all the quiz already ;)")
        else:
            self.quizmodes()

#####spaced repititon purpose for flashcard
    def deactivate_flashcard(self):
        self.flashCard.config(state="disabled")
        self.flashCard.config(text = "You've rivised all the cards")
        Timer(5,self.activate_flashcard).start()

    def activate_flashcard(self):
        self.flashCard.config(state="normal")
        for i in self.deck:
            #convert deck's key to question's key
            self.question.append({"question":i['question_deck'],"answer":i['answer_deck']})
        print(self.question)
        self.progresss -= len(self.deck)
        #update the labels for flashcards
        self.progress.config(text = f"FLASH CARD REVISED :  {self.cardleft} / {self.progresss}")
        self.flashCard.config(text = f"FLASHCARD REMAINS :  {len(self.question)}")

#####Its a flashcard 
    def flashcard(self):
        self.flashcard_top = Toplevel(self.quiz)
        self.flashcard_top.title('FLASHCARD QUIZZER')

        self.flashcard_top.configure(bg= '#4CAF50')

        #set the row and column for top level window
        self.flashcard_top.columnconfigure((0,1,2,3),weight=1)
        self.flashcard_top.rowconfigure((0,1,2,3,4,5),weight=1)

        # Get screen width and height
        screen_width = self.flashcard_top.winfo_screenwidth()
        screen_height = self.flashcard_top.winfo_screenheight()

        #Set top level suze
        create_fc_width = 1000
        create_fc_height = 700

        #Calculation for the position of the top level window
        x_position = (screen_width // 2) - (create_fc_width // 2)
        y_position = (screen_height // 2) - (create_fc_height // 2)

        # Set the geometry of the Toplevel window
        self.flashcard_top.geometry(f"{create_fc_width}x{create_fc_height}+{x_position}+{y_position}")
            
        #To ensure the window wont be smaller than the fixed size
        self.flashcard_top.minsize(1000,700)

        #its a place for flashcard
        #title for flash card
        self.flashcard_top_title = Label(self.flashcard_top,text = 'FLASHCARD',font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.flashcard_top_title.grid(row = 0,column=0,columnspan=4,sticky=W+E+N)

        #choose random card from the deck
        self.random_question = random.choice(self.question)
    
        #question section 
        self.flashcard_top_question = Label(self.flashcard_top,text = "QUESTION : ",font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.flashcard_top_question.grid(row = 1,column=0,sticky=E+N)

        #show the random question
        self.flashcard_top_questions = Label(self.flashcard_top,text = f"{self.random_question['question']}",font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50',wraplength=250)
        self.flashcard_top_questions.grid(row = 1,column=1,columnspan=3,sticky=W+N)

        #show the label of the answers
        self.flashcard_top_answer_label = Label(self.flashcard_top, text = "ANSWER : ",font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.flashcard_top_answer_label.grid(row = 2,column=0,sticky=E+N)

        self.flashcard_return = Button(self.flashcard_top, text = "BACK", font = ('Helvetica',30,'bold'),padx=5, fg = 'white',bg = '#4CAF50', command = self.flashcard_back,bd=0, highlightthickness=0, )
        self.flashcard_return.grid(row = 3,column=0,sticky=N+E)

        self.flashcard_top_answer_button = Button(self.flashcard_top,text = "SHOW ANSWER", font = ('Helvetica',30,'bold'),padx=5,fg = 'white',bg = '#4CAF50',command = self.show_answer_function,bd=0,highlightthickness=0)
        self.flashcard_top_answer_button.grid(row = 3,column=1,columnspan = 4,sticky=N,padx = 20)

    def flashcard_back(self):
        if messagebox.askokcancel("Attention","Are you sure you want to stop revise ? "):
            self.flashcard_top.withdraw()
            self.flashcard_return.config()
        else:
            pass

    def show_answer_function(self):
        #show answer
        self.flashcard_top_answer = Label(self.flashcard_top,text = f"{self.random_question['answer']}",font = ('Helvetica',30,'bold'),pady=10,fg = 'white', bg = '#4CAF50',wraplength=250)
        self.flashcard_top_answer.grid(row = 2,column=1,columnspan=3,sticky=W+N)

        #label to give a hint to user wether they feel the question is hard or not,if hard revise again
        self.hint = Label(self.flashcard_top,text = "* CHOOSE EITHER HARD OR EASY FOR YOU THE QUESTION IS :)",font = ('Helvetica',15,'bold'),fg = 'white',bg = '#4CAF50')
        self.hint.grid(row = 4, column =0,columnspan=4,sticky = W+E+N)

        #these buttons will show after show answer buttong being click
        #hard
        self.hard = Button(self.flashcard_top,text = "HARD",fg = "white", bg = '#4CAF50',            font=('Helvetica',30,'bold'),command = self.hards,bd=0,   highlightthickness=0, )
        self.hard.grid(row = 5,column =0,columnspan=3,sticky = N,padx = 230)

        self.easy = Button(self.flashcard_top,text = "EASY",fg = "white",bg = '#4CAF50',font=('Helvetica',30,'bold'),command = self.easys,bd=0,highlightthickness=0,  )
        self.easy.grid(row = 5,column =2,columnspan=3,sticky = N,padx = 230)

    #hard function
    def hards(self):
        self.flashcard_top_questions.config(text = f"{self.random_question['question']}")
        self.flashcard_top_answer.config(text = "")
        self.hint.grid_forget()
        self.hard.grid_forget()
        self.easy.grid_forget()

    def easys(self):
            self.progresss +=1
            #delete used index
            self.index = self.question.index(self.random_question)
            del self.question[self.index] 
            print(self.question)

            if len(self.question)== 0:
                self.flashCard.config(text = f"FLASHCARD REMAINS :  {len(self.question)}")
                self.progress.config(text = f"FLASH CARD REVISED :  {self.cardleft} / {self.progresss}")
                messagebox.showinfo("Congratz","You've revised all the card congratulations :)")
                self.deactivate_flashcard()
                self.flashcard_top.withdraw()

            else:

                self.flashCard.config(text = f"FLASHCARD REMAINS :  {len(self.question)}")
                self.progress.config(text = f"FLASH CARD REVISED :  {self.cardleft} / {self.progresss}")

                self.random_question = random.choice(self.question)
                self.flashcard_top_questions.config(text = f"{self.random_question['question']}")
                self.flashcard_top_answer.config(text = "")
                self.hint.grid_forget()
                self.hard.grid_forget()
                self.easy.grid_forget()


#####Its a quiz mode 
    def quizmodes(self):
        self.quizmode_top = Toplevel(self.quiz)
        self.quizmode_top.title('QUIZ')

        #set the background color for quiz mode
        self.quizmode_top.configure(bg = '#4CAF50')

        #set the row and column for top level window
        self.quizmode_top.columnconfigure((0,1,2,3),weight=1)
        self.quizmode_top.rowconfigure((0,1,2,3,4,5),weight=1)

        # Get screen width and height
        screen_width = self.quizmode_top.winfo_screenwidth()
        screen_height = self.quizmode_top.winfo_screenheight()

        #Set top level suze
        create_fc_width = 1000
        create_fc_height = 700

        #Calculation for the position of the top level window
        x_position = (screen_width // 2) - (create_fc_width // 2)
        y_position = (screen_height // 2) - (create_fc_height // 2)

        # Set the geometry of the Toplevel window
        self.quizmode_top.geometry(f"{create_fc_width}x{create_fc_height}+{x_position}+{y_position}")
            
        #To ensure the window wont be smaller than the fixed size
        self.quizmode_top.minsize(1000,700)

        #To call the confirmation function
        self.quizmode_top.protocol("WM_DELETE_WINDOW",self.close_quizmode)


        #its a place for flashcard
        #title for flash card
        self.quizmode_title = Label(self.quizmode_top,text = 'QUIZ',font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.quizmode_title.grid(row = 0,column=0,columnspan=4,sticky=W+E+N)

        #choose random card from the deck
        self.random_question_quiz = random.choice(self.question_quiz)

        #question section 
        self.quizmode_question = Label(self.quizmode_top,text = "QUESTION : ", font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.quizmode_question.grid(row = 1,column=0,sticky=E+N)

        #show the random question
        self.quizmode_questions = Label(self.quizmode_top,text = f"{self.random_question_quiz['question_quiz']}",font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.quizmode_questions.grid(row = 1,column=1,columnspan = 3,sticky=W+N)

        #answer section 
        self.quizmode_answer = Label(self.quizmode_top,text = "ANSWER : ",font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.quizmode_answer.grid(row = 2,column=0,sticky=E+N)

        #answer box label
        self.quizmode_answers = Label(self.quizmode_top,text = "ENTER ANSWER : ",font = ('Helvetica',30,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.quizmode_answers.grid(row = 3,column=0,sticky=E+N)

        self.quizmode_answer_box = Entry(self.quizmode_top,font = ('Helvetica',30,'bold'),fg = 'white',bg = '#4CAF50',highlightthickness=0,)
        self.quizmode_answer_box.grid(row = 3,column=1,columnspan=3,sticky=W+N)

        self.quizmode_answer_submit = Button(self.quizmode_top,text = "SUBMIT",font = ('Helvetica',30,'bold'),pady=10,bd=0,highlightthickness=0,command = self.check_answer)
        self.quizmode_answer_submit.grid(row = 4,column=0,columnspan=2,sticky=N) 


    def check_answer(self):
        answer = self.quizmode_answer_box.get().strip()
        chosen_answer = self.random_question_quiz["answer_quiz"]


        if answer == chosen_answer:

            self.quiz_correct += 1
            #show correct sign
            self.quizmode_correct = Label(self.quizmode_top,text = "CORRECT ! ",font = ('Helvetica',30,'bold'),fg = 'white', bg = '#4CAF50')
            self.quizmode_correct.grid(row = 5,column=0,columnspan= 4,sticky=W+E+N)
            #show answer
            self.quizmode_answer = Label(self.quizmode_top,text = f"{self.random_question_quiz['answer_quiz']}",font = ('Helvetica',30,'bold'),pady=10,fg = 'white', bg = '#4CAF50')
            self.quizmode_answer.grid(row = 2,column=1,columnspan = 3,sticky=W+N)

            self.quizmode_answer_next= Button(self.quizmode_top,text = "NEXT",font = ('Helvetica',30,'bold'),pady=10, bd=0,highlightthickness=0,command = self.next_quiz)
            self.quizmode_answer_next.grid(row = 4,column=2,columnspan=2,sticky=N) 

        elif answer == "":
            self.quizmode_wrong = Label(self.quizmode_top,text = "DONT LEAVE THE ANSWER BOX EMPTY",font = ('Helvetica',30,'bold'),fg = 'white',bg = '#4CAF50')
            self.quizmode_wrong.grid(row = 5,column=0,columnspan = 4,sticky=W+E+N)

        elif answer != chosen_answer:
            self.quiz_wrong += 1

            self.quizmode_wrong = Label(self.quizmode_top,text = "WRONG ! ",font = ('Helvetica',30,'bold'),fg = 'white',bg = '#4CAF50')
            self.quizmode_wrong.grid(row = 5,column=0,columnspan = 4,sticky=W+E+N)

            self.quizmode_answer = Label(self.quizmode_top, text = f"{self.random_question_quiz['answer_quiz']}",font = ('Helvetica',30,'bold'),pady=10,fg = 'white', bg = '#4CAF50' )
            self.quizmode_answer.grid(row = 2,column=1,columnspan = 3,sticky=W+N)

            self.quizmode_answer_next= Button(self.quizmode_top,text = "NEXT",font = ('Helvetica',30,'bold'),pady=10,bd=0,highlightthickness=0,command = self.next_quiz)
            self.quizmode_answer_next.grid(row = 4,column=2,columnspan=2,sticky=N) 

    def next_quiz(self):
        #delete used index
        self.index = self.question_quiz.index(self.random_question_quiz)
        del self.question_quiz[self.index] 

        if len(self.question_quiz) == 0:
            messagebox.showinfo("Attention",f"YOU GOT {self.quiz_correct} CORRECT {self.quiz_wrong} WRONG :)")
            self.quiz_correct = 0
            self.quiz_wrong = 0
            self.deactivate_quiz()
            self.quizmode_top.destroy()
        else:
            #pick a new one
            self.random_question_quiz = random.choice(self.question_quiz)
            #update new question
            self.quizmode_questions.config(text = f"{self.random_question_quiz['question_quiz']}")
            #hide next button
            self.quizmode_answer_next.grid_forget()
            #update new answer
            self.quizmode_answer.config(text = "")
            self.quizmode_answer_box.delete(0,END)
            #hide correct/wrong sign
            self.quizmode_correct.grid_forget()
            self.quizmode_wrong.grid_forget()
            print(self.question_quiz)

    #Spaced repitition purpose for quiz    
    def deactivate_quiz(self):
        self.quizmode.config(state="disabled")
        self.quizmode.config(text = "You've answered all the question")
        Timer(5,self.activate_quiz).start()

    def activate_quiz(self):
        self.quizmode.config(state="normal")
        for i in self.deck:
            #convert deck's key to question's key
            self.question_quiz.append({"question_quiz":i['question_deck'],"answer_quiz":i['answer_deck']})
        #update quiz mode main menu function
        self.quizmode.config(text = " QUIZ MODE ")


    #Confirmation of closing add card window
    def close_quizmode(self):
        if messagebox.askokcancel("Quit","Are you sure you want to stop playing quiz ?"):
            self.quizmode_top.destroy()
        else:
            pass


#####Its a place to add new card
    def addcard_section(self):
        #This is to set the toplevel window same at center
        self.addcard_top = Toplevel(self.quiz)
        self.addcard_top.title('Add New Card')

        #set the background color
        self.addcard_top.configure(bg = '#4CAF50')

        #set the row and the column
        self.addcard_top.columnconfigure((0,1,2,3),weight=1)
        self.addcard_top.rowconfigure((0,1,2,3),weight=1)

        # Get screen width and height
        screen_width = self.addcard_top.winfo_screenwidth()
        screen_height = self.addcard_top.winfo_screenheight()

        #Set top level suze
        addcard_top_width = 1000
        addcard_top_height = 700

        #Calculation for the position of the top level window
        x_position = (screen_width // 2) - (addcard_top_width // 2)
        y_position = (screen_height // 2) - (addcard_top_height // 2)

        # Set the geometry of the Toplevel window
        self.addcard_top.geometry(f"{addcard_top_width}x{addcard_top_height}+{x_position}+{y_position}")
            
        #To ensure the window wont be smaller than the fixed size
        self.addcard_top.minsize(1000,700)


        #Add Card title
        self.addCard_title = Label(self.addcard_top, text = 'Add Card',font = ('Helvetica',40,'bold'),pady=10,fg = 'white',bg = '#4CAF50')
        self.addCard_title.grid(row = 0,column=0,columnspan=4,sticky=W+E+N)

        #label to show its question bar
        self.addCard_question_label = Label(self.addcard_top, text = "QUESTION : ",font = ('Helvetica',30,'bold'),fg = 'white',bg = '#4CAF50')
        self.addCard_question_label.grid(row = 1,column=0,sticky = E+N,pady = 60)

        #place to enter question
        self.addCard_question = Entry(self.addcard_top,highlightthickness=0,bg='#4CAF50',fg = 'white',font = ('Arial',30,'bold'))
        self.addCard_question.grid(row = 1,column=1,columnspan=3,sticky='news',padx = 40,pady = 60)

        #label to show its a answer bar
        self.addCard_question_label = Label(self.addcard_top,text = "ANSWER : ",font = ('Helvetica',30,'bold'),fg = 'white',bg = '#4CAF50')
        self.addCard_question_label.grid(row = 2,column=0,sticky=E+N,pady=60)

        #place to enter answer of the question
        self.addCard_answer = Entry(self.addcard_top,highlightthickness=0,bg='#4CAF50',fg = 'white',font = ('Arial',30,'bold'))
        self.addCard_answer.grid(row = 2,column=1,columnspan=3,sticky='news',padx = 40,pady = 60)

        #button for submit the question to store
        self.addCard_submit_button = Button(self.addcard_top,text = 'SUBMIT',font = ('Helvetica',30,'bold'),command = self.track_question_answer,height = 2,width = 15, bd=0,highlightthickness=0,fg = 'white',bg = '#4CAF50')
        self.addCard_submit_button.grid(row = 3,column=2)

        #button for back to main page
        self.addCard_return = Button(self.addcard_top,text = 'BACK',font = ('Helvetica',30,'bold'),command = self.addCard_returnn,height = 2,width = 15,bd=0,highlightthickness=0,fg = 'white',bg = '#4CAF50')
        self.addCard_return.grid(row = 3,column=0)


    def track_question_answer(self):
        questions = self.addCard_question.get().strip()
        ans = self.addCard_answer.get().strip()

        #to detect is the user enter nothing
        if questions == "" and ans == "":
            messagebox.showinfo("Attention","You submmited nothing :(")
        elif questions == "" and ans:
            messagebox.showinfo("Attention","Please don't leave the question empty :(")
        elif ans == "" and questions:
            messagebox.showinfo("Attention","Please don't leave the answer empty :(")
        else:
            #update the amount of deckss and card left as well as quiz'deck counter
            self.cardleft +=1
            #add question and answer into question's list
            self.question.append({"question":questions,"answer":ans})
            self.question_quiz.append({"question_quiz":questions,"answer_quiz":ans})
            self.deck.append({"question_deck":questions,"answer_deck":ans})

            #call save json function
            self.save_quiz()

            #clear the entry box
            self.addCard_question.delete(0,END)
            self.addCard_answer.delete(0,END)
            #update amount
            self.flashCard.config(text=f"FLASHCARD REMAINS: {len(self.question)}")
            self.progress.config(text = f"FLASH CARD REVISED :  {self.cardleft} / {self.progresss}")
            self.deck_check.config(text = f" TOTAL CARD :\n {len(self.deck)}")

            messagebox.showinfo("Congratz","You've added a flashcard successfully :)")

    def addCard_returnn(self):
        if messagebox.askokcancel("Quit","Are you sure you're done adding flashcard?"):
            self.addcard_top.destroy()
        else:
            pass

    #Its a function to save data into Quizz.json file
    def save_quiz(self):
        data = {'progress':self.progresss,
                'cardleft':self.cardleft,
                'question':self.question,
                'question_quiz':self.question_quiz,
                'deck':self.deck
                }
        try:
            with open(self.file_quiz,"w") as file:
                json.dump(data,file,indent =4)
        except (FileNotFoundError,json.JSONDecodeError):
            return

    #Its a function for load the Quizz.json file
    def load_quiz(self):
        try:
            if os.path.exists(self.file_quiz):
                with open(self.file_quiz,"r") as file:
                    data = json.load(file)

                    #to get the data
                    self.progresss = data.get('progress',0)
                    self.cardleft = data.get('cardleft',0)
                    self.question = data.get('question',[])
                    self.question_quiz = data.get('question_quiz',[])
                    self.deck = data.get('deck',[])
        except (FileNotFoundError,json.JSONDecodeError):
            return


###########################################################################################

class Reminder:
    def __init__(self, message, date_str, time_str, recurring, sound_file=None):
        self.message = message
        self.date_str = date_str
        self.time_str = time_str
        self.recurring = recurring
        self.sound_file = sound_file
        self.datetime_obj = datetime.datetime.strptime(f"{date_str} {time_str}", "%Y-%m-%d %I:%M:%S %p")

    def matches_current_time(self):
        now = datetime.datetime.now()
        current_date = now.strftime("%Y-%m-%d")
        current_time = now.strftime("%I:%M:%S %p")
        return self.date_str == current_date and self.time_str == current_time

############################################################################################################
class ReminderApp:
    def __init__(self,master,reminder_instance):
        self.reminder_instance = reminder_instance
        self.master = master

        self.reminders = []
        self.file_name = "reminders.json"  # File to save reminders
        self.sound_files = self.get_sound_files()
        self.current_sound = None

        # Title Section
        title_frame = tk.Frame(self.master, bg="#4CAF50")
        title_frame.pack(fill=tk.X)
        title_label = tk.Label(
            title_frame, text="Reminder App", font=("Helvetica", 24, "bold"), bg="#4CAF50", fg="white"
        )
        title_label.pack(pady=10)

        # Input Section
        input_frame = tk.Frame(self.master, bg="#f0f0f0")
        input_frame.pack(pady=20, padx=20, fill=tk.X)

        tk.Label(input_frame, text="Message:", font=("Helvetica", 14), bg="#010101").grid(row=0, column=0, padx=10, pady=10, sticky=tk.W)
        self.message_entry = tk.Entry(input_frame, width=50, font=("Helvetica", 12))
        self.message_entry.grid(row=0, column=1, columnspan=3, padx=10, pady=10, sticky=tk.W)

        tk.Label(input_frame, text="Date:", font=("Helvetica", 14), bg="#000000").grid(row=1, column=0, padx=10, pady=10, sticky=tk.W)
        self.date_picker = DateEntry(input_frame, width=15, background="#4CAF50", foreground="white", date_pattern="yyyy-MM-dd")
        self.date_picker.grid(row=1, column=1, padx=10, pady=10, sticky=tk.W)

        tk.Label(input_frame, text="Time:", font=("Helvetica", 14), bg="#030303").grid(row=1, column=2, padx=10, pady=10, sticky=tk.W)
        self.hour_combobox = ttk.Combobox(input_frame, values=[f"{i:02d}" for i in range(1, 13)], width=5, font=("Helvetica", 12))
        self.hour_combobox.grid(row=1, column=3, padx=5, pady=10, sticky=tk.W)
        self.hour_combobox.set("12")

        self.minute_combobox = ttk.Combobox(input_frame, values=[f"{i:02d}" for i in range(0, 60)], width=5, font=("Helvetica", 12))
        self.minute_combobox.grid(row=1, column=4, padx=5, pady=10, sticky=tk.W)
        self.minute_combobox.set("00")

        self.period_combobox = ttk.Combobox(input_frame, values=["AM", "PM"], width=5, font=("Helvetica", 12))
        self.period_combobox.grid(row=1, column=5, padx=5, pady=10, sticky=tk.W)
        self.period_combobox.set("AM")

        # Music Selection Section
        tk.Label(input_frame, text="Reminder Sound:", font=("Helvetica", 14), bg="#f0f0f0").grid(row=2, column=0, padx=10, pady=10, sticky=tk.W)
        
        # Combobox for predefined sounds
        self.sound_combobox = ttk.Combobox(input_frame, values=self.sound_files, width=30, font=("Helvetica", 12))
        self.sound_combobox.grid(row=2, column=1, columnspan=2, padx=10, pady=10, sticky=tk.W)
        
        # Button to add custom sound
        self.custom_sound_button = ttk.Button(input_frame, text="Add Custom Sound", command=self.add_custom_sound)
        self.custom_sound_button.grid(row=2, column=3, padx=10, pady=10, sticky=tk.W)

        self.recurring_var = tk.BooleanVar()
        tk.Checkbutton(
            input_frame, text="Recurring", variable=self.recurring_var, font=("Helvetica", 12), bg="#f0f0f0"
        ).grid(row=3, column=0, padx=10, pady=10, sticky=tk.W)

        # Buttons
        button_frame = tk.Frame(self.master, bg="#f0f0f0")
        button_frame.pack(pady=10)

        self.add_button = ttk.Button(button_frame, text="Add Reminder", command=self.add_reminder)
        self.add_button.grid(row=0, column=0, padx=10, pady=10)

        self.delete_button = ttk.Button(button_frame, text="Delete Reminder", command=self.delete_reminder)
        self.delete_button.grid(row=0, column=1, padx=10, pady=10)

        # Reminder List
        self.tree = ttk.Treeview(
            self.master, columns=("Message", "Date", "Time", "Recurring", "Sound"), show="headings"
        )
        self.tree.heading("Message", text="Message")
        self.tree.heading("Date", text="Date")
        self.tree.heading("Time", text="Time")
        self.tree.heading("Recurring", text="Recurring")
        self.tree.heading("Sound", text="Sound")
        self.tree.column("Message", width=250, anchor=tk.W)
        self.tree.column("Date", width=100, anchor=tk.CENTER)
        self.tree.column("Time", width=100, anchor=tk.CENTER)
        self.tree.column("Recurring", width=100, anchor=tk.CENTER)
        self.tree.column("Sound", width=150, anchor=tk.CENTER)
        self.tree.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)

        # Load reminders
        self.load_reminders()

        # Start checking reminders
        threading.Thread(target=self.check_reminders, daemon=True).start()

    def get_sound_files(self):
        """Retrieve sound files from the sound directory."""
        try:
            # Create sound directory if it doesn't exist
            os.makedirs("sound", exist_ok=True)
            
            # Get MP3 files from the sound directory
            sound_files = [f for f in os.listdir("sound") if f.endswith(".mp3")]
            
            # Add a default option
            sound_files.insert(0, "Default Sound")
            
            return sound_files
        except Exception as e:
            print(f"Error retrieving sound files: {e}")
            return ["Default Sound"]

    def add_custom_sound(self):
        """Allow user to add a custom sound file."""
        filetypes = [('MP3 files', '*.mp3')]
        custom_sound = filedialog.askopenfilename(title="Select Sound File", filetypes=filetypes)
        
        if custom_sound:
            try:
                # Copy the file to the sound directory
                sound_name = os.path.basename(custom_sound)
                destination = os.path.join("sound", sound_name)
                
                # If file doesn't already exist in sound directory
                if not os.path.exists(destination):
                    import shutil
                    shutil.copy(custom_sound, destination)
                
                # Update sound files list and combobox
                self.sound_files = self.get_sound_files()
                self.sound_combobox['values'] = self.sound_files
                self.sound_combobox.set(sound_name)
            except Exception as e:
                messagebox.showerror("Error", f"Could not add sound file: {e}")

    def play_sound(self, sound_file=None):
        """Play the selected sound file."""
        try:
            # If no specific sound file is provided, use the combobox selection
            if sound_file is None:
                sound_file = self.sound_combobox.get()
            
            # Handle default sound
            if sound_file == "Default Sound" or not sound_file:
                sound_path = "sound\\reminder app sound.mp3"
            else:
                sound_path = os.path.join("sound", sound_file)
            
            # Load and play the sound
            pygame.mixer.music.load(sound_path)
            pygame.mixer.music.play()
        except Exception as e:
            messagebox.showerror("Sound Error", f"Could not play sound: {e}")

    def add_reminder(self):
        message = self.message_entry.get().strip()
        date_str = self.date_picker.get().strip()
        hour_str = self.hour_combobox.get().strip()
        minute_str = self.minute_combobox.get().strip()
        period_str = self.period_combobox.get().strip()
        sound_file = self.sound_combobox.get()

        if not message:
            messagebox.showerror("Error", "Reminder message cannot be empty.")
            return

        if not date_str or not hour_str or not minute_str or not period_str:
            messagebox.showerror("Error", "Please select a valid date and time.")
            return

        time_str = f"{hour_str}:{minute_str}:00 {period_str}"

        try:
            datetime.datetime.strptime(f"{date_str} {time_str}", "%Y-%m-%d %I:%M:%S %p")
        except ValueError:
            messagebox.showerror("Error", "Invalid date or time format.")
            return

        recurring = self.recurring_var.get()
        reminder = Reminder(message, date_str, time_str, recurring, sound_file)
        self.reminders.append(reminder)
        self.save_reminders()

        self.tree.insert("", "end", values=(message, date_str, time_str, "Yes" if recurring else "No", sound_file))
        messagebox.showinfo("Success", "Reminder added successfully!")
        self.message_entry.delete(0, tk.END)

    def delete_reminder(self):
        selected_items = self.tree.selection()
        if not selected_items:
            messagebox.showerror("Error", "Please select a reminder to delete.")
            return

        for item in selected_items:
            values = self.tree.item(item, "values")
            self.tree.delete(item)

            for reminder in self.reminders[:]:
                if (reminder.message == values[0] and 
                    reminder.date_str == values[1] and 
                    reminder.time_str == values[2]):
                    self.reminders.remove(reminder)

        self.save_reminders()
        messagebox.showinfo("Success", "Selected reminders deleted successfully.")

    def save_reminders(self):
        reminders_data = [
            {
                "message": reminder.message,
                "date_str": reminder.date_str,
                "time_str": reminder.time_str,
                "recurring": reminder.recurring,
                "sound_file": reminder.sound_file,
            }
            for reminder in self.reminders
        ]
        with open(self.file_name, "w") as f:
            json.dump(reminders_data, f)

    def load_reminders(self):
        try:
            with open(self.file_name, "r") as f:
                reminders_data = json.load(f)
                for reminder_data in reminders_data:
                    reminder = Reminder(
                        reminder_data["message"],
                        reminder_data["date_str"],
                        reminder_data["time_str"],
                        reminder_data.get("recurring", False),
                        reminder_data.get("sound_file", "Default Sound")
                    )
                    self.reminders.append(reminder)
                    self.tree.insert(
                        "", "end", 
                        values=(
                            reminder.message, 
                            reminder.date_str, 
                            reminder.time_str, 
                            "Yes" if reminder.recurring else "No",
                            reminder.sound_file
                        )
                    )
        except FileNotFoundError:
            pass

    def check_reminders(self):
        while True:
            now = datetime.datetime.now()
            for reminder in self.reminders[:]:
                if reminder.matches_current_time():
                    # Pass the sound file to play_sound method
                    self.master.after(0, self.show_reminder_message, reminder)
                    if not reminder.recurring:
                        self.reminders.remove(reminder)
                        self.save_reminders()
            time.sleep(1)

    def show_reminder_message(self, reminder):
        # Play the specific sound for this reminder
        self.play_sound(reminder.sound_file)
        messagebox.showinfo("Reminder", reminder.message)

###########################################################################################
class PomodoroApp:
    def __init__(self, master):
        self.master = master

        # Timer settings (default values)
        self.work_duration = 25 * 60
        self.break_duration = 10 * 60
        self.long_break_duration = 30 * 60
        self.cycles_before_long_break = 4

        self.is_running = False
        self.is_work_time = True
        self.remaining_time = self.work_duration
        self.pomodoro_count = 0
        self.timer_id = None

        # self.master.configure(bg = '#4CAF50')
        self.master.columnconfigure((0,1,2,3),weight=1)
        self.master.rowconfigure((0,1,2,3),weight=1)

        self.main_label = Label(self.master,text = 'POMODORO TIMER',fg = 'white',bg= '#4CAF50',font = ('Helvetica',50,'bold'))
        self.main_label.grid(row = 0,column = 0,columnspan=4,sticky='news')

        # Timer Display
        self.timer_display = Label(self.master, text="25:00", font=("Helvetica", 110,'bold'),bg = '#4CAF50',fg = 'white')
        self.timer_display.grid(row = 1,column = 0,columnspan=4,sticky = 'news')

        self.frame_wsl = Frame(self.master)
        self.frame_wsl.grid(row=2,columnspan=4,sticky='news')
        self.frame_wsl.config(bg = '#4CAF50')
        self.frame_wsl.columnconfigure((0,1,2,3,4,5),weight=1)
        self.frame_wsl.rowconfigure((0),weight=1)

        #Work Buttons
        self.worklabel = Label(self.frame_wsl,text = '',bg = '#4CAF50')
        self.worklabel.grid(row = 2,column = 0,sticky='news')
        self.work_button = Button(self.frame_wsl, bg = '#4CAF50',fg = 'white',text="Work", pady = 3,padx = 15,command=self.set_work_mode,font = ('Arial',30,'bold'),bd=0,highlightthickness=0)
        self.work_button.grid(row=0, column=0,columnspan=2,sticky=E)

        #Short break button
        self.shortlabel = Label(self.frame_wsl,text = '',bg = '#4CAF50')
        self.shortlabel.grid(row = 0,column = 2,sticky='news')
        self.short_break_button = Button(self.frame_wsl, bg = '#4CAF50',fg = 'white',text="Short Break", pady = 3,padx = 15,command=self.set_short_break_mode,font = ('Arial',30,'bold'),bd=0,highlightthickness=0)
        self.short_break_button.grid(row=0, column=2,columnspan=2)

        #Long break button
        self.longlabel = Label(self.frame_wsl,text = '',bg = '#4CAF50')
        self.longlabel.grid(row = 2,column = 4,columnspan=2,sticky='news')
        self.long_break_button = Button(self.frame_wsl, bg = '#4CAF50',fg = 'white',text="Long Break", pady = 3,padx = 15,command=self.set_long_break_mode,font = ('Arial',30,'bold'),bd=0,highlightthickness=0)
        self.long_break_button.grid(row=0, column=4,columnspan=2,sticky = 'W')

        #frame for start and reset button
        # Start Button
        self.frame_sr = Frame(self.master)
        self.frame_sr.grid(row=3,columnspan=4,sticky='news')
        self.frame_sr.config(bg = '#4CAF50')
        self.frame_sr.columnconfigure((0,1,2,3),weight=1)
        self.frame_sr.rowconfigure((0),weight=1)



        self.startlabel = Label(self.frame_sr,text = '',bg = '#4CAF50')
        self.startlabel.grid(column = 0,columnspan=2,sticky='news')
        self.start_button = Button(self.frame_sr, bg = '#4CAF50',fg = 'white',text="Start", pady = 3,padx = 15,command=self.toggle_timer,font = ('Arial',30,'bold'),bd=0,highlightthickness=0)
        self.start_button.grid(row = 0,column=0,columnspan=2)

        #Reset Button
        self.resetlabel = Label(self.frame_sr,text = '',bg = '#4CAF50')
        self.resetlabel.grid(column = 2,columnspan=2,sticky='news')
        self.reset_button = Button(self.frame_sr,bg = '#4CAF50',fg = 'white', text="Reset", pady = 3,padx = 15,command=self.reset_timer,font = ('Arial',30,'bold'),bd=0,highlightthickness=0)
        self.reset_button.grid(row = 0,column=2,columnspan=2)

    def toggle_timer(self):
        if not self.is_running:
            self.start_timer()
        else:
            self.pause_timer()

    def start_timer(self):
        self.is_running = True
        self.start_button.config(text="Pause")
        self.update_timer()

    def pause_timer(self):
        if self.timer_id:
            self.master.after_cancel(self.timer_id)
            self.timer_id = None
        self.is_running = False
        self.start_button.config(text="Start")

    def update_timer(self):
        if not self.is_running:
            return

        minutes = self.remaining_time // 60
        seconds = self.remaining_time % 60
        self.timer_display.config(text=f"{minutes:02}:{seconds:02}")

        if self.remaining_time > 0:
            self.remaining_time -= 1
            self.timer_id = self.master.after(1000, self.update_timer)
        else:
            self.timer_complete()

    def timer_complete(self):
        self.is_running = False
        self.start_button.config(text="Start")

        if self.is_work_time:
            self.pomodoro_count += 1
            messagebox.showinfo("Pomodoro Complete", "Time for a break!")
            self.set_short_break_mode()
        else:
            messagebox.showinfo("Break Over", "Time to get back to work!")
            self.set_work_mode()

    def reset_timer(self):
        if self.timer_id:
            self.master.after_cancel(self.timer_id)
            self.timer_id = None

        self.is_running = False
        self.remaining_time = self.work_duration if self.is_work_time else self.break_duration
        self.timer_display.config(text="25:00")
        self.start_button.config(text="Start")

    def set_work_mode(self):
        self.is_work_time = True
        self.remaining_time = self.work_duration
        self.timer_display.config(text="25:00")

    def set_short_break_mode(self):
        self.is_work_time = False
        self.remaining_time = self.break_duration
        self.timer_display.config(text="05:00")

    def set_long_break_mode(self):
        self.is_work_time = False
        self.remaining_time = self.long_break_duration
        self.timer_display.config(text="15:00")


###############################################################################################

#To set title of the window and adjust the window size
class StudentBoosterApp():
    def __init__(self,master):
        self.window = master

        self.window.title('Student Bootser App')

        # Define the window size
        window_width = 1000
        window_height = 700

        self.window.configure(bg = "#4CAF50")

        # Get screen width and height
        screen_width = self.window.winfo_screenwidth()
        screen_height = self.window.winfo_screenheight()

        # Calculate x and y offsets for centering
        x_offset = (screen_width - window_width) // 2
        y_offset = (screen_height - window_height) // 2

        # Set window size and position
        self.window.geometry(f"{window_width}x{window_height}+{x_offset}+{y_offset}")
        
        #To ensure the window wont be smaller than the fixed size
        self.window.minsize(1000,700)

        #its to show the exit message
        self.window.protocol('WM_DELETE_WINDOW',self.show_exit)

        #its a notebook function to create tabs
        self.notebook = ttk.Notebook(self.window)
        self.notebook.pack(expand= True,fill= 'both')

        #Default Settings forReminder Function
        self.date_str = "2024-12-12"  # default date
        self.time_str = "08:00:00 AM"  # default time
        self.recurring = True

        self.tab_select()

        messagebox.showinfo("Student Booster App Greeting","WELCOME TO STUDENT BOOSTER APP :)")

        self.window.mainloop()


    #its for the main menu changing tab
    def tab_select(self):
        #adding tab for flashcard quizzer section
        flashcard_tab = ttk.Frame(self.notebook)
        self.notebook.add(flashcard_tab, text="FLASHCARD QUIZZER")
        Quizz(flashcard_tab)

        #adding tab for reminder section
        task_reminder_tab = ttk.Frame(self.notebook)
        self.notebook.add(task_reminder_tab, text="TASK REMINDER")
        reminder_instance = Reminder(message="Default Reminder", 
                              date_str=self.date_str, 
                              time_str=self.time_str, 
                              recurring=self.recurring)  #Reminder needs these arguments
        ReminderApp(task_reminder_tab, reminder_instance)  #ReminderApp needs an instance

        #adding pomodoro timer tab 
        pomodoro_tab = ttk.Frame(self.notebook)
        self.notebook.add(pomodoro_tab, text="POMODORO TIMER")
        PomodoroApp(pomodoro_tab)
    
    def show_exit(self):
        if messagebox.askokcancel("Quit","Do you want to exit the 'Student Booster App'?"):
            self.window.destroy()
        else:
            #it will do nothing and return to the app
            pass

#To run the window
if __name__ == "__main__":
    root = Tk()
    app = StudentBoosterApp(root)
    root.mainloop()


