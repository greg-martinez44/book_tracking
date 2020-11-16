import tkinter as tk
from tkinter import ttk
import csv
import os
from datetime import datetime

class LabelInput(tk.Frame):
    def __init__(
        self,
        parent,
        label="",
        input_class=tk.Entry,
        input_args=None,
        input_var=None,
        label_args=None,
        **kwargs
        ):
        super().__init__(parent, **kwargs)
        input_args = input_args or {}
        label_args = label_args or {}
        self.variable = input_var

        if input_class in (ttk.Radiobutton, ttk.Checkbutton, ttk.Button):
            input_args["text"] = label
            input_args["variable"] = input_var
        else:
            self.label = tk.Label(self, text=label, font="Verdana", **label_args)
            self.label.grid(row=0, column=0, sticky=(tk.W + tk.E))
            input_args["textvariable"] = input_var
        
        self.input = input_class(self, **input_args)
        self.input.grid(row=1, column=0, sticky=(tk.W + tk.E))

        self.columnconfigure(0, weight=1)

        self.error = getattr(self.input, "error", tk.StringVar())
        self.error_label = ttk.Label(self, textvariable=self.error)
        self.error_label.grid(row=2, column=0, sticky=(tk.W + tk.E))

    def grid(self, sticky=(tk.W + tk.E), **kwargs):
        super().grid(sticky=sticky, **kwargs)

    def get(self):
        try:
            if self.variable:
                return self.variable.get()
            elif isinstance(self.input, tk.Text):
                return self.input.get("1.0", tk.END)
            elif isinstance(self.input, tk.Listbox):
                return self.input.get(0, tk.END)
            else:
                return self.input.get()
        except (TypeError, TclError):
            return ""

    def set(self, value, *args, **kwargs):
        if isinstance(self.variable, tk.BooleanVar):
            self.variable.set(bool(value))
        elif self.variable:
            self.variable.set(value, *args, **kwargs)
        elif isinstance(self.input, tk.Text):
            self.input.delete("1.0", tk.END)
            self.input.insert("1.0", value)
        elif isinstance(self.input, (ttk.Radiobutton, ttk.Checkbox)):
            if value:
                self.input.select()
            else:
                self.input.deselect()
        elif isinstance(self.input, ttk.Button):
            pass
        else:
            self.input.delete(0, tk.END)
            self.input.insert(0, value)


class BookDataEntryForm(tk.Frame):
    def __init__(self, parent, *args, **kwargs):
        super().__init__(parent, *args, **kwargs)

        self.inputs = {}

        self.build_metadata_frame()
        self.build_metrics_frame()
        self.build_feedback_frame()

    def build_metadata_frame(self):
        metadata_frame = tk.LabelFrame(self, text="Metadata")

        self.inputs["title"] = LabelInput(
            metadata_frame,
            label="Title",
            input_class=RequiredEntry,
            input_var=tk.StringVar(),
            )
        self.inputs["title"].grid(row=0, column=0)

        self.inputs["authors"] = LabelInput(
            metadata_frame,
            label="Author(s)",
            input_class=RequiredEntry,
            input_var=tk.StringVar()
            )
        self.inputs["authors"].grid(row=1, column=0)

        self.inputs["publisher"] = LabelInput(
            metadata_frame,
            label="Publisher",
            input_class=RequiredEntry,
            input_var=tk.StringVar(),
            )
        self.inputs["publisher"].grid(row=0, column=2)

        self.inputs["publish_date"] = LabelInput(
            metadata_frame,
            input_class=DateEntry,
            label="Publish Date",
            input_var=tk.StringVar()
            )
        self.inputs["publish_date"].grid(row=2, column=0)

        self.inputs["page_count"] = LabelInput(
            metadata_frame,
            label="Page Count",
            input_class=RequiredEntry,
            input_var=tk.IntVar()
            )
        self.inputs["page_count"].grid(row=1, column=2)

        self.inputs["genre"] = LabelInput(
            metadata_frame,
            label="Genre",
            input_var=tk.StringVar()
            )
        self.inputs["genre"].grid(row=2, column=2)

        metadata_frame.grid(row=0, column=0, padx=10, sticky=(tk.W + tk.E))

    def build_metrics_frame(self):
        metrics_frame = tk.LabelFrame(self, text="Metrics")

        self.inputs["start_date"] = LabelInput(
            metrics_frame,
            label="Start Date",
            input_class=DateEntry,
            input_var=tk.StringVar(),
            )
        self.inputs["start_date"].grid(row=0, column=0, padx=10)

        self.inputs["finish_date"] = LabelInput(
            metrics_frame,
            label="Finish Date",
            input_class=DateEntry,
            input_var=tk.StringVar(),
            )
        self.inputs["finish_date"].grid(row=1, column=0, padx=10)

        self.inputs["reread"] = LabelInput(
            metrics_frame,
            label="Reread?",
            input_class=ttk.Checkbutton,
            input_var=tk.BooleanVar(),
            )
        self.inputs["reread"].grid(row=0, column=1)
        
        self.inputs["dnf"] = LabelInput(
            metrics_frame,
            label="DNF?",
            input_class=ttk.Checkbutton,
            input_var=tk.BooleanVar()
            )
        self.inputs["dnf"].grid(row=1, column=1)

        metrics_frame.grid(row=1, column=0, ipadx=30, sticky=(tk.W + tk.E))

    def build_feedback_frame(self):
        feedback_frame = tk.LabelFrame(self, text="Feedback")

        self.inputs["rating"] = LabelInput(
            feedback_frame,
            label="Rating",
            input_class=ValidateCombobox,
            input_var=tk.IntVar(),
            input_args={
                "values": [1, 2, 3, 4, 5]
                }
            )
        self.inputs["rating"].grid(row=0, column=0)

        self.inputs["notes"] = LabelInput(
            feedback_frame,
            label="Quick Thoughts",
            input_class=tk.Text,
            input_args={
                "width": 50,
                "height": 10,
                "font": "Verdana",
                }
            )
        self.inputs["notes"].grid(row=1, column=0)

        feedback_frame.grid(row=2, column=0, sticky=(tk.W + tk.E))

    def get(self):
        return {key: widget.get() for key, widget in self.inputs.items()}

    def reset(self):
        for widget in self.inputs.values():
            widget.set("")

    def get_errors(self):
        errors = {}
        for key, widget in self.inputs.items():
            if hasattr(widget.input, "trigger_focusout_validation"):
                widget.input.trigger_focusout_validation()
            if widget.error.get():
                errors[key] = widget.error.get()
        return errors

class ValidateMixin:
    def __init__(self, *args, error_var=None, **kwargs):
        self.error = error_var or tk.StringVar()
        super().__init__(*args, **kwargs)

        vcmd = self.register(self._validate)
        invcmd = self.register(self._invalid)

        self.config(
            validate="all",
            validatecommand=(vcmd, "%P", "%s", "%S", "%V", "%i", "%d"),
            invalidcommand=(invcmd, "%P", "%s", "%S", "%V", "%i", "%d"),
            )
    def _toggle_error(self, on=False):
        self.config(foreground=("red" if on else "black"))

    def _validate(self, proposed, current, char, event, index, action):
        self._toggle_error(False)
        self.error.set("")
        valid = True
        if event == "focusout":
            valid = self._focusout_validate(event=event)
        elif event == "key":
            valid = self._key_validate(
                proposed=proposed,
                current=current,
                char=char,
                event=event,
                index=index,
                action=action
                )
        return valid

    def _focusout_validate(self, **kwargs):
        return True
    
    def _key_validate(self, **kwargs):
        return True
    
    def _invalid(self, proposed, current, char, event, index, action):
        if event == "focusout":
            self._focusout_invalid(event=event)
        elif event == "key":
            self._key_invalid(
                proposed=proposed,
                current=current,
                char=char,
                event=event,
                index=index,
                action=action
                )
    def _focusout_invalid(self, **kwargs):
        self._toggle_error(True)
    
    def _key_invalid(self, **kwargs):
        pass

    def trigger_focusout_validation(self):
        valid = self._validate("", "", "", "focusout", "", "")
        if not valid:
            self._focusout_invalid(event="focusout")
        return valid

class RequiredEntry(ValidateMixin, ttk.Entry):
    def _focusout_validate(self, event):
        valid = True
        if not self.get():
            valid = False
            self.error.set("A value is required")
        return valid

class DateEntry(ValidateMixin, ttk.Entry):
    def _key_validate(self, action, index, char, **kwargs):
        valid = True

        if action == "0":
            valid = True
        elif index in ("0", "1", "2", "3", "5", "6", "8", "9"):
            valid = char.isdigit()
        elif index in ("4", "7"):
            valid = char == "-"
        else:
            valid = False
        return valid
    
    def _focusout_validate(self, event):
        valid = True
        if not self.get():
            self.error.set("A value is required")
        try:
            datetime.strptime(self.get(), "%Y-%m-%d")
        except ValueError:
            self.error.set("Invalid date")
            valid = False
        return valid

class ValidateCombobox(ValidateMixin, ttk.Combobox):
    def _key_validate(self, proposed, action, **kwargs):
        valid = True
        if action == "0":
            self.set("")
            return True
        values = self.cget("values")
        matching = [
            x for x in values
            if x.lower().startswith(proposed.lower())
            ]
        if len(matching) == 0:
            valid = False
        elif len(matching) == 1:
            self.set(matching[0])
            self.icursor(tk.END)
            valid = False
        return valid

    def _focusout_validate(self, **kwargs):
        valid = True
        if not self.get():
            valid = False
            self.error.set("A value is required")
        return valid

class BookDataEntryApp(tk.Tk):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.title("Book Entry Form")
        self.attributes("-fullscreen", False)
        self.full_screen_state = False
        self.bind("<F10>", self._enter_full_screen)
        self.bind("<Escape>", self._quit_full_screen)

        ttk.Label(
            self,
            text="Book Entry",
            font=("Verdana", 16)
            ).grid(row=0)

        self.entry_form = BookDataEntryForm(self)
        self.entry_form.grid(row=1, padx=10)
        
        self.save_button = ttk.Button(
            self,
            text="Save",
            command=self.on_save
            )
        self.save_button.grid(row=2, sticky=(tk.W + tk.E), padx=10)
        
        self.status = tk.StringVar()
        self.statusbar = ttk.Label(self, textvariable=self.status)
        self.statusbar.grid(row=3, sticky=(tk.W + tk.E), padx=10)

    def _enter_full_screen(self, event):
        self.full_screen_state = not self.full_screen_state
        self.attributes("-fullscreen", self.full_screen_state)
    def _quit_full_screen(self, event):
        self.full_screen_state = False
        self.attributes("-fullscreen", self.full_screen_state)

    def on_save(self):
        errors = self.entry_form.get_errors()
        if errors:
            self.status.set(
                f"Cannot save error in fields: {', '.join(errors.keys())}"
                )
            return False
        data = self.entry_form.get()
        file = "/Users/gregmartinez/projects/book_tracking/books.csv"
        newfile = not os.path.exists(file)
        with open(file, "a") as csvfile:
            csvwriter = csv.DictWriter(csvfile, fieldnames=data.keys())
            if newfile:
                csvwriter.writeheader()
            csvwriter.writerow(data)


if __name__ == "__main__":
    app = BookDataEntryApp()
    app.mainloop()


