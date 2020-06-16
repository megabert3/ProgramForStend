package org.taipit.stend.controller;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.taipit.stend.model.ExcelReport;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainStend {
    public TreeMap<String, Integer> tree;

    public MainStend() {
        tree = new TreeMap<>(comparatorForinflABC);
    }

    public static void main(String[] args) {
        //new ExcelReport().createExcelReport();

        //F;55;A;L;0,5;Imax;0.02
        MainStend mainStend = new MainStend();

        mainStend.tree.put("U;100;A;L;0,5;Imax;1.0", 0);
        mainStend.tree.put("U;99;A;L;0,5;Imax;1.0", 0);
        mainStend.tree.put("F;100;A;L;0,5;Imax;1.0", 0);
        mainStend.tree.put("F;99;A;L;0,5;Imax;1.0", 0);

        for (Map.Entry<String, Integer> elem : mainStend.tree.entrySet()) {
            System.out.println(elem.getKey());
        }
    }

    Comparator<String> comparatorForinflABC = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //F;55;A;L;0,5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("U") && arrO2[0].equals("F")) {
                return 1;
            } else if (arrO1[0].equals("F") && arrO2[0].equals("U")) {
                return -1;
            } else if (arrO1[0].equals("U") && arrO2[0].equals("U")) {
                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return 1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return -1;
                } if (Float.parseFloat(arrO1[1]) == Float.parseFloat(arrO2[1])) {
                    if (arrO1[2].equals("A") && !arrO2[2].equals("A")) {
                        return 1;
                    } else if (!arrO1[2].equals("A") && arrO2[2].equals("A")) {
                        return -1;
                    } else if (arrO1[2].equals("A") && arrO2[2].equals("A")) {
                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return 1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return -1;
                        } else if (arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return 1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return -1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("L")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("C")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        }
                    } else if (arrO1[2].equals("B") && arrO2[2].equals("C")) {
                        return 1;
                    } else if (arrO1[2].equals("C") && arrO2[2].equals("B")) {
                        return -1;
                    } else if (arrO1[2].equals("B") && arrO2[2].equals("B")) {
                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return 1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return -1;
                        } else if (arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return 1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return -1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("L")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("C")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        }
                    } if (arrO1[2].equals("C") && arrO2[2].equals("C")) {
                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return 1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return -1;
                        } else if (arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return 1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return -1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("L")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("C")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (arrO1[0].equals("F") && arrO2[0].equals("F")) {
                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return 1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return -1;
                } if (Float.parseFloat(arrO1[1]) == Float.parseFloat(arrO2[1])) {
                    if (arrO1[2].equals("A") && !arrO2[2].equals("A")) {
                        return 1;
                    } else if (!arrO1[2].equals("A") && arrO2[2].equals("A")) {
                        return -1;
                    } else if (arrO1[2].equals("A") && arrO2[2].equals("A")) {
                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return 1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return -1;
                        } else if (arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return 1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return -1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("L")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("C")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        }
                    } else if (arrO1[2].equals("B") && arrO2[2].equals("C")) {
                        return 1;
                    } else if (arrO1[2].equals("C") && arrO2[2].equals("B")) {
                        return -1;
                    } else if (arrO1[2].equals("B") && arrO2[2].equals("B")) {
                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return 1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return -1;
                        } else if (arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return 1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return -1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("L")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("C")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        }
                    } if (arrO1[2].equals("C") && arrO2[2].equals("C")) {
                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return 1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return -1;
                        } else if (arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return 1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return -1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("L")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("C")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) == Float.parseFloat(arrO2[4])) {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return 1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return -1;
                                } else if (arrO1[5].equals("Imax") && arrO2.equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                } else if (arrO1[5].equals("Ib") && arrO2.equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return 1;
        }
    };
}

