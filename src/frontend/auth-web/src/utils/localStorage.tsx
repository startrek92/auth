export function saveToLocalStorage<T>(key: string, value: T): void {
    try {
        if (typeof value === 'object' && value !== null) {
            localStorage.setItem(key, JSON.stringify(value));
        } else {
            localStorage.setItem(key, value as unknown as string);
        }
    } catch (error) {
        console.error(`Error saving to localStorage: ${error}`);
    }
}

export function getFromLocalStorage<T>(key: string): T | null {
    const value = localStorage.getItem(key);
    try {
        if (value) {
            const parsedValue = JSON.parse(value);
            return typeof parsedValue === 'object' ? parsedValue as T : parsedValue as T;
        }
        return null;
    } catch (error) {
        console.error(`Error parsing from localStorage: ${error}`);
        return null; 
    }
}

export function removeFromLocalStorage(key: string): void {
    if (typeof key === 'string') {
        localStorage.removeItem(key);
    } else {
        console.error(`Invalid key type: ${typeof key}`);
    }
}
